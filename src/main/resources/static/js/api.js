/* ==============================================================================
   Vehicle Rental & Fleet Management System - Core API Client & State
   ============================================================================== */

const API_BASE = '/api';

const Auth = {
  getToken() {
    return localStorage.getItem('vr_token');
  },
  setToken(token) {
    localStorage.setItem('vr_token', token);
  },
  getUser() {
    const userStr = localStorage.getItem('vr_user');
    try {
      return userStr ? JSON.parse(userStr) : null;
    } catch (e) {
      return null;
    }
  },
  setUser(user) {
    localStorage.setItem('vr_user', JSON.stringify(user));
  },
  clear() {
    localStorage.removeItem('vr_token');
    localStorage.removeItem('vr_user');
  },
  isLoggedIn() {
    return !!this.getToken();
  },
  isAdmin() {
    const user = this.getUser();
    return user && user.role === 'ROLE_ADMIN';
  },
  isCustomer() {
    const user = this.getUser();
    return user && user.role === 'ROLE_CUSTOMER';
  },
  requireAuth(requiredRole = null) {
    if (!this.isLoggedIn()) {
      window.location.href = '/login.html?redirect=' + encodeURIComponent(window.location.pathname);
      return false;
    }
    if (requiredRole && this.getUser()?.role !== requiredRole) {
      showToast('Access denied: Unauthorized role', 'error');
      setTimeout(() => {
        window.location.href = '/index.html';
      }, 1200);
      return false;
    }
    return true;
  }
};

// Unified Fetch API
async function apiFetch(endpoint, options = {}) {
  const url = endpoint.startsWith('http') ? endpoint : `${API_BASE}${endpoint}`;
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  const token = Auth.getToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers
  };

  try {
    const response = await fetch(url, config);
    const data = await response.json().catch(() => null);

    if (!response.ok) {
      if (response.status === 401) {
        Auth.clear();
        if (!window.location.pathname.includes('login.html')) {
          showToast('Session expired. Please sign in again.', 'warning');
          setTimeout(() => {
            window.location.href = '/login.html';
          }, 1000);
        }
      }
      const errorMsg = data?.message || data?.error || `Request failed with status ${response.status}`;
      throw new Error(errorMsg);
    }

    return data;
  } catch (err) {
    console.error(`API Error on [${options.method || 'GET'} ${endpoint}]:`, err);
    throw err;
  }
}

// API Service Endpoints
const API = {
  // Authentication
  auth: {
    async login(email, password) {
      const res = await apiFetch('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password })
      });
      if (res.data && res.data.token) {
        Auth.setToken(res.data.token);
        Auth.setUser(res.data);
      }
      return res;
    },
    async register(formData) {
      return await apiFetch('/auth/register', {
        method: 'POST',
        body: JSON.stringify(formData)
      });
    },
    async getProfile() {
      return await apiFetch('/auth/me');
    },
    async updateProfile(profileData) {
      return await apiFetch('/auth/profile', {
        method: 'PUT',
        body: JSON.stringify(profileData)
      });
    }
  },

  // Vehicles
  vehicles: {
    async getAll(params = {}) {
      const query = new URLSearchParams();
      for (const [key, val] of Object.entries(params)) {
        if (val !== undefined && val !== null && val !== '') {
          query.append(key, val);
        }
      }
      const qs = query.toString() ? `?${query.toString()}` : '';
      return await apiFetch(`/vehicles${qs}`);
    },
    async getById(id) {
      return await apiFetch(`/vehicles/${id}`);
    },
    async checkAvailability(id, startDate, endDate, excludeBookingId = null) {
      let url = `/vehicles/${id}/check-availability?startDate=${startDate}&endDate=${endDate}`;
      if (excludeBookingId) url += `&excludeBookingId=${excludeBookingId}`;
      return await apiFetch(url);
    },
    async create(vehicleData) {
      return await apiFetch('/vehicles', {
        method: 'POST',
        body: JSON.stringify(vehicleData)
      });
    },
    async update(id, vehicleData) {
      return await apiFetch(`/vehicles/${id}`, {
        method: 'PUT',
        body: JSON.stringify(vehicleData)
      });
    },
    async delete(id) {
      return await apiFetch(`/vehicles/${id}`, {
        method: 'DELETE'
      });
    },
    async updateStatus(id, status) {
      return await apiFetch(`/vehicles/${id}/status?status=${status}`, {
        method: 'PATCH'
      });
    }
  },

  // Bookings
  bookings: {
    async create(bookingData) {
      return await apiFetch('/bookings', {
        method: 'POST',
        body: JSON.stringify(bookingData)
      });
    },
    async getAll() {
      return await apiFetch('/bookings');
    },
    async getMyBookings() {
      return await apiFetch('/bookings/my-bookings');
    },
    async getById(id) {
      return await apiFetch(`/bookings/${id}`);
    },
    async updateStatus(id, status, reason = '') {
      return await apiFetch(`/bookings/${id}/status`, {
        method: 'PUT',
        body: JSON.stringify({ status, reason })
      });
    },
    async cancel(id, reason = 'Cancelled by user') {
      return await apiFetch(`/bookings/${id}/cancel`, {
        method: 'PUT',
        body: JSON.stringify({ reason })
      });
    }
  },

  // Rentals (Dispatches & Returns)
  rentals: {
    async getAll() {
      return await apiFetch('/rentals');
    },
    async getActive() {
      return await apiFetch('/rentals/active');
    },
    async getMyRentals() {
      return await apiFetch('/rentals/my-rentals');
    },
    async getById(id) {
      return await apiFetch(`/rentals/${id}`);
    },
    async getByBookingId(bookingId) {
      return await apiFetch(`/rentals/booking/${bookingId}`);
    },
    async start(rentalData) {
      return await apiFetch('/rentals', {
        method: 'POST',
        body: JSON.stringify(rentalData)
      });
    },
    async returnVehicle(id, returnData) {
      return await apiFetch(`/rentals/${id}/return`, {
        method: 'PUT',
        body: JSON.stringify(returnData)
      });
    }
  },

  // Payments
  payments: {
    async process(paymentData) {
      return await apiFetch('/payments', {
        method: 'POST',
        body: JSON.stringify(paymentData)
      });
    },
    async getMyPayments() {
      return await apiFetch('/payments/my-payments');
    },
    async getAll() {
      return await apiFetch('/payments');
    },
    async getByBooking(bookingId) {
      return await apiFetch(`/payments/booking/${bookingId}`);
    }
  },

  // Maintenance
  maintenance: {
    async getAll() {
      return await apiFetch('/maintenance');
    },
    async getById(id) {
      return await apiFetch(`/maintenance/${id}`);
    },
    async getByVehicle(vehicleId) {
      return await apiFetch(`/maintenance/vehicle/${vehicleId}`);
    },
    async schedule(data) {
      return await apiFetch('/maintenance', {
        method: 'POST',
        body: JSON.stringify(data)
      });
    },
    async update(id, data) {
      return await apiFetch(`/maintenance/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data)
      });
    },
    async updateStatus(id, status) {
      return await apiFetch(`/maintenance/${id}/status?status=${status}`, {
        method: 'PATCH'
      });
    },
    async delete(id) {
      return await apiFetch(`/maintenance/${id}`, {
        method: 'DELETE'
      });
    }
  },

  // Dashboard Stats & Admin Management
  admin: {
    async getStats() {
      return await apiFetch('/admin/stats');
    },
    async getCustomers() {
      return await apiFetch('/admin/customers');
    }
  },
  customer: {
    async getStats() {
      return await apiFetch('/customer/stats');
    }
  }
};

// UI Utilities: Toast Notifications
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;

  const iconMap = {
    success: 'bi-check-circle-fill',
    error: 'bi-x-circle-fill',
    warning: 'bi-exclamation-triangle-fill',
    info: 'bi-info-circle-fill'
  };
  const icon = iconMap[type] || 'bi-info-circle-fill';

  toast.innerHTML = `
    <i class="bi ${icon}"></i>
    <div>${message}</div>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

// Formatters
function formatCurrency(val) {
  if (val === null || val === undefined) return '₹0';
  return '₹' + Number(val).toLocaleString('en-IN');
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  try {
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch (e) {
    return dateStr;
  }
}

// Dynamic Navigation Sync
function updateNavbar() {
  const navActions = document.getElementById('nav-actions');
  const user = Auth.getUser();

  if (!navActions) return;

  if (Auth.isLoggedIn() && user) {
    const dashboardLink = Auth.isAdmin() ? '/admin/dashboard.html' : '/customer/dashboard.html';
    const roleBadge = Auth.isAdmin() ? 'Admin' : 'Customer';

    navActions.innerHTML = `
      <div style="display: flex; align-items: center; gap: 0.75rem;">
        <a href="${dashboardLink}" class="btn btn-secondary btn-sm">
          <i class="bi bi-speedometer2"></i> Dashboard
        </a>
        <div style="display: flex; flex-direction: column; align-items: flex-end; font-size: 0.8rem;">
          <span style="color: var(--text-main); font-weight: 600;">${user.name}</span>
          <span class="badge ${Auth.isAdmin() ? 'badge-rented' : 'badge-available'}" style="font-size: 0.65rem; padding: 0.1rem 0.4rem;">${roleBadge}</span>
        </div>
        <button onclick="handleLogout()" class="btn btn-outline btn-sm" title="Sign Out">
          <i class="bi bi-box-arrow-right"></i>
        </button>
      </div>
    `;
  } else {
    navActions.innerHTML = `
      <a href="/login.html" class="btn btn-outline btn-sm">Sign In</a>
      <a href="/register.html" class="btn btn-primary btn-sm">Register</a>
    `;
  }
}

function handleLogout() {
  Auth.clear();
  showToast('You have been signed out.', 'info');
  setTimeout(() => {
    window.location.href = '/index.html';
  }, 800);
}

// Auto init when document is loaded
document.addEventListener('DOMContentLoaded', () => {
  updateNavbar();

  // Mobile menu toggle
  const toggle = document.querySelector('.mobile-toggle');
  const navLinks = document.querySelector('.nav-links');
  if (toggle && navLinks) {
    toggle.addEventListener('click', () => {
      navLinks.classList.toggle('show');
    });
  }
});
