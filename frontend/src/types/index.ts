export interface Patient {
  uid?: number;
  firstName: string;
  lastName: string;
  birthday: string; // LocalDate as string in format YYYY-MM-DD
  mobileNo: string;
  email: string;
  address: string;
  gender: 'MALE' | 'FEMALE';
  emergencyContactName: string;
  emergencyContactPhone: string;
  insuranceInfo: string;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  username: string | null;
}

export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  birthday: string; // LocalDate as string in format YYYY-MM-DD
}

export interface HealthResponse {
  status: string;
  message: string;
}

export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
}