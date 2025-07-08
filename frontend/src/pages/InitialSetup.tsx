import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { apiService } from '../services/api';
import { CreateUserRequest } from '../types';
import './InitialSetup.css';

export const InitialSetup: React.FC = () => {
  const navigate = useNavigate();
  const [userCount, setUserCount] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');
  const [checkingSystem, setCheckingSystem] = useState(true);

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm<CreateUserRequest & { confirmPassword: string }>();

  const password = watch('password');

  useEffect(() => {
    checkSystemStatus();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const checkSystemStatus = async () => {
    try {
      setCheckingSystem(true);
      const response = await apiService.getUserCount();
      setUserCount(response.data);
      
      // If users already exist, redirect to login
      if (response.data > 0) {
        navigate('/login');
      }
    } catch (err) {
      setError('Failed to check system status');
      console.error('System check error:', err);
    } finally {
      setCheckingSystem(false);
    }
  };

  const onSubmit = async (data: CreateUserRequest & { confirmPassword: string }) => {
    setLoading(true);
    setError('');

    try {
      const { confirmPassword, ...userData } = data;
      await apiService.createAdmin(userData);
      alert('Admin user created successfully! You can now log in.');
      navigate('/login');
    } catch (err) {
      setError('Failed to create admin user. Please try again.');
      console.error('Admin creation error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (checkingSystem) {
    return <div className="loading-container">Checking system status...</div>;
  }

  if (userCount > 0) {
    return <div className="loading-container">Redirecting to login...</div>;
  }

  return (
    <div className="initial-setup">
      <div className="setup-card">
        <div className="setup-header">
          <h1>Patient Management System</h1>
          <h2>Initial Setup</h2>
          <p>Welcome! Let's create your first admin account to get started.</p>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit(onSubmit)} className="setup-form">
          <div className="form-section">
            <h3>Personal Information</h3>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="firstName">First Name *</label>
                <input
                  id="firstName"
                  type="text"
                  {...register('firstName', { required: 'First name is required' })}
                  className={errors.firstName ? 'error' : ''}
                  placeholder="Enter your first name"
                />
                {errors.firstName && (
                  <span className="field-error">{errors.firstName.message}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="lastName">Last Name *</label>
                <input
                  id="lastName"
                  type="text"
                  {...register('lastName', { required: 'Last name is required' })}
                  className={errors.lastName ? 'error' : ''}
                  placeholder="Enter your last name"
                />
                {errors.lastName && (
                  <span className="field-error">{errors.lastName.message}</span>
                )}
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="birthday">Date of Birth *</label>
              <input
                id="birthday"
                type="date"
                {...register('birthday', { required: 'Date of birth is required' })}
                className={errors.birthday ? 'error' : ''}
              />
              {errors.birthday && (
                <span className="field-error">{errors.birthday.message}</span>
              )}
            </div>
          </div>

          <div className="form-section">
            <h3>Account Information</h3>
            <div className="form-group">
              <label htmlFor="username">Username *</label>
              <input
                id="username"
                type="text"
                {...register('username', { 
                  required: 'Username is required',
                  minLength: { value: 3, message: 'Username must be at least 3 characters' }
                })}
                className={errors.username ? 'error' : ''}
                placeholder="Choose a username"
              />
              {errors.username && (
                <span className="field-error">{errors.username.message}</span>
              )}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="password">Password *</label>
                <input
                  id="password"
                  type="password"
                  {...register('password', { 
                    required: 'Password is required',
                    minLength: { value: 6, message: 'Password must be at least 6 characters' }
                  })}
                  className={errors.password ? 'error' : ''}
                  placeholder="Create a strong password"
                />
                {errors.password && (
                  <span className="field-error">{errors.password.message}</span>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password *</label>
                <input
                  id="confirmPassword"
                  type="password"
                  {...register('confirmPassword', { 
                    required: 'Please confirm your password',
                    validate: value => value === password || 'Passwords do not match'
                  })}
                  className={errors.confirmPassword ? 'error' : ''}
                  placeholder="Confirm your password"
                />
                {errors.confirmPassword && (
                  <span className="field-error">{errors.confirmPassword.message}</span>
                )}
              </div>
            </div>
          </div>

          <div className="setup-actions">
            <button
              type="submit"
              disabled={loading}
              className="create-button"
            >
              {loading ? 'Creating Admin Account...' : 'Create Admin Account'}
            </button>
          </div>
        </form>

        <div className="setup-footer">
          <p>This will create the first administrator account for your system.</p>
          <p>You can create additional users later from the admin panel.</p>
        </div>
      </div>
    </div>
  );
};