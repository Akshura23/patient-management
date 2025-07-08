import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/api';
import { Patient } from '../types';
import './PatientList.css';

export const PatientList: React.FC = () => {
  const navigate = useNavigate();
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [sortBy, setSortBy] = useState<'firstName' | 'lastName' | 'email' | 'createdDate'>('firstName');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = async () => {
    try {
      setLoading(true);
      const response = await apiService.getAllPatients();
      setPatients(response.data);
    } catch (err) {
      setError('Failed to load patients');
      console.error('Patients fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (patientId: number) => {
    if (window.confirm('Are you sure you want to delete this patient?')) {
      try {
        await apiService.deletePatient(patientId);
        setPatients(patients.filter(p => p.uid !== patientId));
      } catch (err) {
        alert('Failed to delete patient');
        console.error('Delete error:', err);
      }
    }
  };

  const filteredAndSortedPatients = patients
    .filter(patient => 
      patient.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.mobileNo.includes(searchTerm)
    )
    .sort((a, b) => {
      const aValue = a[sortBy] || '';
      const bValue = b[sortBy] || '';
      
      if (sortOrder === 'asc') {
        return aValue < bValue ? -1 : aValue > bValue ? 1 : 0;
      } else {
        return aValue > bValue ? -1 : aValue < bValue ? 1 : 0;
      }
    });

  const handleSort = (field: typeof sortBy) => {
    if (sortBy === field) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortBy(field);
      setSortOrder('asc');
    }
  };

  if (loading) {
    return <div className="loading-container">Loading patients...</div>;
  }

  if (error) {
    return <div className="error-container">{error}</div>;
  }

  return (
    <div className="patient-list">
      <div className="patient-list-header">
        <h1>Patients</h1>
        <button 
          className="add-patient-button"
          onClick={() => navigate('/patients/new')}
        >
          Add New Patient
        </button>
      </div>

      <div className="patient-list-controls">
        <div className="search-container">
          <input
            type="text"
            placeholder="Search patients..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>
        
        <div className="sort-container">
          <label>Sort by:</label>
          <select 
            value={`${sortBy}-${sortOrder}`}
            onChange={(e) => {
              const [field, order] = e.target.value.split('-');
              setSortBy(field as typeof sortBy);
              setSortOrder(order as 'asc' | 'desc');
            }}
            className="sort-select"
          >
            <option value="firstName-asc">First Name (A-Z)</option>
            <option value="firstName-desc">First Name (Z-A)</option>
            <option value="lastName-asc">Last Name (A-Z)</option>
            <option value="lastName-desc">Last Name (Z-A)</option>
            <option value="email-asc">Email (A-Z)</option>
            <option value="email-desc">Email (Z-A)</option>
            <option value="createdDate-desc">Newest First</option>
            <option value="createdDate-asc">Oldest First</option>
          </select>
        </div>
      </div>

      {filteredAndSortedPatients.length > 0 ? (
        <div className="patients-table-container">
          <table className="patients-table">
            <thead>
              <tr>
                <th onClick={() => handleSort('firstName')}>
                  Name {sortBy === 'firstName' && (sortOrder === 'asc' ? '↑' : '↓')}
                </th>
                <th onClick={() => handleSort('email')}>
                  Email {sortBy === 'email' && (sortOrder === 'asc' ? '↑' : '↓')}
                </th>
                <th>Phone</th>
                <th>Gender</th>
                <th>Age</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredAndSortedPatients.map((patient) => (
                <tr key={patient.uid}>
                  <td>{patient.firstName} {patient.lastName}</td>
                  <td>{patient.email}</td>
                  <td>{patient.mobileNo}</td>
                  <td>{patient.gender}</td>
                  <td>
                    {patient.birthday ? 
                      Math.floor((Date.now() - new Date(patient.birthday).getTime()) / (365.25 * 24 * 60 * 60 * 1000))
                      : 'N/A'
                    }
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button 
                        className="view-btn"
                        onClick={() => navigate(`/patients/${patient.uid}`)}
                      >
                        View
                      </button>
                      <button 
                        className="edit-btn"
                        onClick={() => navigate(`/patients/${patient.uid}/edit`)}
                      >
                        Edit
                      </button>
                      <button 
                        className="delete-btn"
                        onClick={() => handleDelete(patient.uid!)}
                      >
                        Delete
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="no-patients">
          <p>No patients found.</p>
          {searchTerm && (
            <p>Try adjusting your search criteria.</p>
          )}
          <button 
            className="add-patient-button"
            onClick={() => navigate('/patients/new')}
          >
            Add First Patient
          </button>
        </div>
      )}
    </div>
  );
};