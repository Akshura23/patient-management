import axios, { AxiosResponse } from 'axios';
import { Patient, LoginRequest, LoginResponse, CreateUserRequest, HealthResponse } from '../types';
import { mockApiService } from './mockApi';

const API_BASE_URL = process.env.REACT_APP_API_URL || '/api';
const DEMO_MODE = process.env.REACT_APP_DEMO_MODE === 'true' || !process.env.REACT_APP_API_URL;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for authentication if needed
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('currentUser');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

const realApiService = {
  // Health check
  health: (): Promise<AxiosResponse<HealthResponse>> => 
    api.get('/health'),

  // Authentication
  login: (loginData: LoginRequest): Promise<AxiosResponse<LoginResponse>> => 
    api.post('/auth/login', loginData),

  // User management
  createAdmin: (userData: CreateUserRequest): Promise<AxiosResponse<string>> => 
    api.post('/users/create-admin', userData),
  
  getUserCount: (): Promise<AxiosResponse<number>> => 
    api.get('/users/count'),

  // Patient management
  getAllPatients: (): Promise<AxiosResponse<Patient[]>> => 
    api.get('/patients'),
  
  getPatientById: (id: number): Promise<AxiosResponse<Patient>> => 
    api.get(`/patients/${id}`),
  
  createPatient: (patient: Omit<Patient, 'uid' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>): Promise<AxiosResponse<Patient>> => 
    api.post('/patients', patient),
  
  updatePatient: (id: number, patient: Omit<Patient, 'uid' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>): Promise<AxiosResponse<Patient>> => 
    api.put(`/patients/${id}`, patient),
  
  deletePatient: (id: number): Promise<AxiosResponse<void>> => 
    api.delete(`/patients/${id}`),
};

// Export the appropriate service based on demo mode
export const apiService = DEMO_MODE ? mockApiService : realApiService;

export default apiService;