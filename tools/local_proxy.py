import socket
import select
import threading
import struct
import sys

DNS_CACHE = {
    'localhost': '127.0.0.1',
    '127.0.0.1': '127.0.0.1'
}

def resolve_dns(domain, dns_servers=['8.8.8.8', '1.1.1.1']):
    if domain in DNS_CACHE:
        return DNS_CACHE[domain]
    
    # Check if already an IP
    try:
        socket.inet_aton(domain)
        return domain
    except:
        pass

    for dns_server in dns_servers:
        try:
            tid = 0x5678
            flags = 0x0100  # recursive query
            qname = b''.join(bytes([len(p)]) + p.encode('latin1') for p in domain.split('.')) + b'\x00'
            packet = struct.pack('!HHHHHH', tid, flags, 1, 0, 0, 0) + qname + struct.pack('!HH', 1, 1)

            sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            sock.settimeout(3.0)
            sock.sendto(packet, (dns_server, 53))
            data, _ = sock.recvfrom(4096)
            sock.close()

            pos = 12
            while pos < len(data) and data[pos] != 0:
                pos += 1 + data[pos]
            pos += 5

            ancount = struct.unpack('!H', data[6:8])[0]
            ips = []

            for _ in range(ancount):
                if pos >= len(data):
                    break
                if (data[pos] & 0xC0) == 0xC0:
                    pos += 2
                else:
                    while pos < len(data) and data[pos] != 0:
                        pos += 1 + data[pos]
                    pos += 1

                if pos + 10 > len(data):
                    break
                rtype, rclass, ttl, rdlength = struct.unpack('!HHIH', data[pos:pos+10])
                pos += 10

                if rtype == 1 and rdlength == 4:  # A record
                    ip = socket.inet_ntoa(data[pos:pos+4])
                    ips.append(ip)
                pos += rdlength

            if ips:
                DNS_CACHE[domain] = ips[0]
                return ips[0]
        except Exception:
            continue

    try:
        ip = socket.gethostbyname(domain)
        DNS_CACHE[domain] = ip
        return ip
    except Exception as e:
        print(f"[DNS FAIL] Could not resolve {domain}: {e}", flush=True)
        return None

def handle_client(client_sock):
    try:
        request_line = b""
        while b"\r\n" not in request_line:
            chunk = client_sock.recv(1)
            if not chunk:
                client_sock.close()
                return
            request_line += chunk

        req_str = request_line.decode('latin1', errors='replace').strip()
        parts = req_str.split()
        if len(parts) < 3:
            client_sock.close()
            return

        method, target, version = parts[0], parts[1], parts[2]

        headers = b""
        while b"\r\n\r\n" not in headers:
            chunk = client_sock.recv(1024)
            if not chunk:
                break
            headers += chunk

        if method == "CONNECT":
            host, port_str = target.split(":") if ":" in target else (target, "443")
            port = int(port_str)
            resolved_ip = resolve_dns(host)
            if not resolved_ip:
                client_sock.sendall(b"HTTP/1.1 502 Bad Gateway\r\n\r\n")
                client_sock.close()
                return

            remote_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            remote_sock.settimeout(15.0)
            remote_sock.connect((resolved_ip, port))
            client_sock.sendall(b"HTTP/1.1 200 Connection Established\r\n\r\n")

            sockets = [client_sock, remote_sock]
            remote_sock.setblocking(False)
            client_sock.setblocking(False)

            while True:
                readable, _, exceptional = select.select(sockets, [], sockets, 60)
                if exceptional or not readable:
                    break
                for s in readable:
                    other = remote_sock if s is client_sock else client_sock
                    try:
                        data = s.recv(16384)
                        if not data:
                            return
                        other.sendall(data)
                    except:
                        return
        else:
            if "://" in target:
                target_url = target.split("://", 1)[1]
                host_part = target_url.split("/", 1)[0]
            else:
                host_part = target

            host, port_str = host_part.split(":") if ":" in host_part else (host_part, "80")
            port = int(port_str)
            resolved_ip = resolve_dns(host)
            if not resolved_ip:
                client_sock.sendall(b"HTTP/1.1 502 Bad Gateway\r\n\r\n")
                client_sock.close()
                return

            remote_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            remote_sock.settimeout(15.0)
            remote_sock.connect((resolved_ip, port))

            full_req = request_line + headers
            remote_sock.sendall(full_req)

            sockets = [client_sock, remote_sock]
            remote_sock.setblocking(False)
            client_sock.setblocking(False)

            while True:
                readable, _, exceptional = select.select(sockets, [], sockets, 60)
                if exceptional or not readable:
                    break
                for s in readable:
                    other = remote_sock if s is client_sock else client_sock
                    try:
                        data = s.recv(16384)
                        if not data:
                            return
                        other.sendall(data)
                    except:
                        return
    except Exception:
        pass
    finally:
        try:
            client_sock.close()
        except:
            pass

def main():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(('127.0.0.1', 8888))
    server.listen(128)
    print("Local DNS-resolving Proxy listening on 127.0.0.1:8888", flush=True)

    while True:
        client_sock, _ = server.accept()
        t = threading.Thread(target=handle_client, args=(client_sock,), daemon=True)
        t.start()

if __name__ == '__main__':
    main()
