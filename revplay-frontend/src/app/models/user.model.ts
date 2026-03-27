export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  role: 'LISTENER' | 'ARTIST' | 'ADMIN';
  isActive: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  role: 'LISTENER' | 'ARTIST';
}

export interface AuthResponse {
  token: string;
  user: User;
  expiresIn: number;
}
