import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { apiService } from '../services/api';
import { Patient } from '../types';
import './PatientDetail.css';

export const PatientDetail: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [patient, setPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (id) {
      fetchPatient(parseInt(id));
    }
  }, [id]);

  const fetchPatient = async (patientId: number) => {
    try {
      setLoading(true);
      const response = await apiService.getPatientById(patientId);
      setPatient(response.data);
    } catch (err) {
      setError('Failed to load patient data');
      console.error('Patient fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (patient && window.confirm('Are you sure you want to delete this patient?')) {
      try {
        await apiService.deletePatient(patient.uid!);
        navigate('/patients');
      } catch (err) {
        alert('Failed to delete patient');
        console.error('Delete error:', err);
      }
    }
  };

  const calculateAge = (birthday: string): number => {
    return Math.floor((Date.now() - new Date(birthday).getTime()) / (365.25 * 24 * 60 * 60 * 1000));
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (loading) {
    return <div className="loading-container">Loading patient details...</div>;
  }

  if (error || !patient) {
    return (
      <div className="error-container">
        <p>{error || 'Patient not found'}</p>
        <button onClick={() => navigate('/patients')}>Back to Patients</button>
      </div>
    );
  }

  return (
    <div className="patient-detail">
      <div className="patient-detail-header">
        <div className="header-left">
          <h1>{patient.firstName} {patient.lastName}</h1>
          <p className="patient-id">Patient ID: {patient.uid}</p>
        </div>
        <div className="header-actions">
          <button 
            className="edit-button"
            onClick={() => navigate(`/patients/${patient.uid}/edit`)}
          >
            Edit Patient
          </button>
          <button 
            className="delete-button"
            onClick={handleDelete}
          >
            Delete Patient
          </button>
          <button 
            className="back-button"
            onClick={() => navigate('/patients')}
          >
            Back to List
          </button>
        </div>
      </div>

      <div className="patient-content">
        <div className="info-section">
          <h3>Personal Information</h3>
          <div className="info-grid">
            <div className="info-item">
              <label>Full Name</label>
              <span>{patient.firstName} {patient.lastName}</span>
            </div>
            <div className="info-item">
              <label>Date of Birth</label>
              <span>{formatDate(patient.birthday)}</span>
            </div>
            <div className="info-item">
              <label>Age</label>
              <span>{calculateAge(patient.birthday)} years old</span>
            </div>
            <div className="info-item">
              <label>Gender</label>
              <span>{patient.gender}</span>
            </div>
          </div>
        </div>

        <div className="info-section">
          <h3>Contact Information</h3>
          <div className="info-grid">
            <div className="info-item">
              <label>Email</label>
              <span>
                <a href={`mailto:${patient.email}`}>{patient.email}</a>
              </span>
            </div>
            <div className="info-item">
              <label>Mobile Number</label>
              <span>
                <a href={`tel:${patient.mobileNo}`}>{patient.mobileNo}</a>
              </span>
            </div>
            <div className="info-item full-width">
              <label>Address</label>
              <span>{patient.address}</span>
            </div>
          </div>
        </div>

        <div className="info-section">
          <h3>Emergency Contact</h3>
          <div className="info-grid">
            <div className="info-item">
              <label>Emergency Contact Name</label>
              <span>{patient.emergencyContactName}</span>
            </div>
            <div className="info-item">
              <label>Emergency Contact Phone</label>
              <span>
                <a href={`tel:${patient.emergencyContactPhone}`}>
                  {patient.emergencyContactPhone}
                </a>
              </span>
            </div>
          </div>
        </div>

        <div className="info-section">
          <h3>Insurance Information</h3>
          <div className="info-grid">
            <div className="info-item full-width">
              <label>Insurance Details</label>
              <span className="insurance-text">{patient.insuranceInfo}</span>
            </div>
          </div>
        </div>

        {(patient.createdDate || patient.lastModifiedDate) && (
          <div className="info-section">
            <h3>Record Information</h3>
            <div className="info-grid">
              {patient.createdDate && (
                <div className="info-item">
                  <label>Created Date</label>
                  <span>{formatDate(patient.createdDate)}</span>
                </div>
              )}
              {patient.createdBy && (
                <div className="info-item">
                  <label>Created By</label>
                  <span>{patient.createdBy}</span>
                </div>
              )}
              {patient.lastModifiedDate && (
                <div className="info-item">
                  <label>Last Modified</label>
                  <span>{formatDate(patient.lastModifiedDate)}</span>
                </div>
              )}
              {patient.lastModifiedBy && (
                <div className="info-item">
                  <label>Last Modified By</label>
                  <span>{patient.lastModifiedBy}</span>
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};