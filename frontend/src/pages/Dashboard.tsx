import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/api';
import { Patient } from '../types';
import './Dashboard.css';

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const [patients, setPatients] = useState<Patient[]>([]);
  const [userCount, setUserCount] = useState<number>(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        const [patientsResponse, userCountResponse] = await Promise.all([
          apiService.getAllPatients(),
          apiService.getUserCount()
        ]);
        
        setPatients(patientsResponse.data);
        setUserCount(userCountResponse.data);
      } catch (err) {
        setError('Failed to load dashboard data');
        console.error('Dashboard data fetch error:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const recentPatients = patients.slice(0, 5);

  if (loading) {
    return <div className="loading-container">Loading dashboard...</div>;
  }

  if (error) {
    return <div className="error-container">{error}</div>;
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Dashboard</h1>
        <p>Welcome to the Patient Management System</p>
      </div>

      <div className="dashboard-stats">
        <div className="stat-card">
          <h3>Total Patients</h3>
          <p className="stat-number">{patients.length}</p>
        </div>
        <div className="stat-card">
          <h3>System Users</h3>
          <p className="stat-number">{userCount}</p>
        </div>
        <div className="stat-card">
          <h3>New Patients Today</h3>
          <p className="stat-number">
            {patients.filter(p => {
              const today = new Date().toISOString().split('T')[0];
              return p.createdDate && p.createdDate.split('T')[0] === today;
            }).length}
          </p>
        </div>
      </div>

      <div className="dashboard-actions">
        <button 
          className="action-button primary"
          onClick={() => navigate('/patients/new')}
        >
          Add New Patient
        </button>
        <button 
          className="action-button secondary"
          onClick={() => navigate('/patients')}
        >
          View All Patients
        </button>
      </div>

      <div className="dashboard-section">
        <h2>Recent Patients</h2>
        {recentPatients.length > 0 ? (
          <div className="recent-patients">
            {recentPatients.map((patient) => (
              <div key={patient.uid} className="patient-card">
                <div className="patient-info">
                  <h4>{patient.firstName} {patient.lastName}</h4>
                  <p>Email: {patient.email}</p>
                  <p>Phone: {patient.mobileNo}</p>
                  <p>Gender: {patient.gender}</p>
                </div>
                <button 
                  className="view-button"
                  onClick={() => navigate(`/patients/${patient.uid}`)}
                >
                  View Details
                </button>
              </div>
            ))}
          </div>
        ) : (
          <div className="no-patients">
            <p>No patients found. Add your first patient to get started!</p>
            <button 
              className="action-button primary"
              onClick={() => navigate('/patients/new')}
            >
              Add First Patient
            </button>
          </div>
        )}
      </div>
    </div>
  );
};