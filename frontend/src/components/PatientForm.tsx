import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { apiService } from '../services/api';
import { Patient } from '../types';
import './PatientForm.css';

type PatientFormData = Omit<Patient, 'uid' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>;

export const PatientForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = Boolean(id);
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');
  const [initialLoad, setInitialLoad] = useState(isEdit);

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
  } = useForm<PatientFormData>();

  useEffect(() => {
    if (isEdit && id) {
      fetchPatient(parseInt(id));
    } else {
      setInitialLoad(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isEdit, id]);

  const fetchPatient = async (patientId: number) => {
    try {
      setInitialLoad(true);
      const response = await apiService.getPatientById(patientId);
      const patient = response.data;
      
      // Set form values
      setValue('firstName', patient.firstName);
      setValue('lastName', patient.lastName);
      setValue('birthday', patient.birthday);
      setValue('mobileNo', patient.mobileNo);
      setValue('email', patient.email);
      setValue('address', patient.address);
      setValue('gender', patient.gender);
      setValue('emergencyContactName', patient.emergencyContactName);
      setValue('emergencyContactPhone', patient.emergencyContactPhone);
      setValue('insuranceInfo', patient.insuranceInfo);
    } catch (err) {
      setError('Failed to load patient data');
      console.error('Patient fetch error:', err);
    } finally {
      setInitialLoad(false);
    }
  };

  const onSubmit = async (data: PatientFormData) => {
    setLoading(true);
    setError('');

    try {
      if (isEdit && id) {
        await apiService.updatePatient(parseInt(id), data);
      } else {
        await apiService.createPatient(data);
      }
      navigate('/patients');
    } catch (err) {
      setError(isEdit ? 'Failed to update patient' : 'Failed to create patient');
      console.error('Patient save error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (initialLoad) {
    return <div className="loading-container">Loading patient data...</div>;
  }

  return (
    <div className="patient-form">
      <div className="patient-form-header">
        <h1>{isEdit ? 'Edit Patient' : 'Add New Patient'}</h1>
        <button 
          className="back-button"
          onClick={() => navigate('/patients')}
        >
          Back to Patients
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit(onSubmit)} className="form">
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
              />
              {errors.lastName && (
                <span className="field-error">{errors.lastName.message}</span>
              )}
            </div>
          </div>

          <div className="form-row">
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

            <div className="form-group">
              <label htmlFor="gender">Gender *</label>
              <select
                id="gender"
                {...register('gender', { required: 'Gender is required' })}
                className={errors.gender ? 'error' : ''}
              >
                <option value="">Select Gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
              </select>
              {errors.gender && (
                <span className="field-error">{errors.gender.message}</span>
              )}
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Contact Information</h3>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                id="email"
                type="email"
                {...register('email', { 
                  required: 'Email is required',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: 'Invalid email address'
                  }
                })}
                className={errors.email ? 'error' : ''}
              />
              {errors.email && (
                <span className="field-error">{errors.email.message}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="mobileNo">Mobile Number *</label>
              <input
                id="mobileNo"
                type="tel"
                {...register('mobileNo', { required: 'Mobile number is required' })}
                className={errors.mobileNo ? 'error' : ''}
              />
              {errors.mobileNo && (
                <span className="field-error">{errors.mobileNo.message}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="address">Address *</label>
            <textarea
              id="address"
              rows={3}
              {...register('address', { required: 'Address is required' })}
              className={errors.address ? 'error' : ''}
            />
            {errors.address && (
              <span className="field-error">{errors.address.message}</span>
            )}
          </div>
        </div>

        <div className="form-section">
          <h3>Emergency Contact</h3>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="emergencyContactName">Emergency Contact Name *</label>
              <input
                id="emergencyContactName"
                type="text"
                {...register('emergencyContactName', { required: 'Emergency contact name is required' })}
                className={errors.emergencyContactName ? 'error' : ''}
              />
              {errors.emergencyContactName && (
                <span className="field-error">{errors.emergencyContactName.message}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="emergencyContactPhone">Emergency Contact Phone *</label>
              <input
                id="emergencyContactPhone"
                type="tel"
                {...register('emergencyContactPhone', { required: 'Emergency contact phone is required' })}
                className={errors.emergencyContactPhone ? 'error' : ''}
              />
              {errors.emergencyContactPhone && (
                <span className="field-error">{errors.emergencyContactPhone.message}</span>
              )}
            </div>
          </div>
        </div>

        <div className="form-section">
          <h3>Insurance Information</h3>
          <div className="form-group">
            <label htmlFor="insuranceInfo">Insurance Information *</label>
            <textarea
              id="insuranceInfo"
              rows={3}
              {...register('insuranceInfo', { required: 'Insurance information is required' })}
              className={errors.insuranceInfo ? 'error' : ''}
              placeholder="Include insurance provider, policy number, and any relevant details"
            />
            {errors.insuranceInfo && (
              <span className="field-error">{errors.insuranceInfo.message}</span>
            )}
          </div>
        </div>

        <div className="form-actions">
          <button
            type="button"
            className="cancel-button"
            onClick={() => navigate('/patients')}
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={loading}
            className="submit-button"
          >
            {loading ? 'Saving...' : (isEdit ? 'Update Patient' : 'Create Patient')}
          </button>
        </div>
      </form>
    </div>
  );
};