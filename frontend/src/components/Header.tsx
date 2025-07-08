import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import './Header.css';

export const Header: React.FC = () => {
  const navigate = useNavigate();
  const { currentUser, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="header-content">
        <div className="header-left">
          <h1 className="header-title" onClick={() => navigate('/dashboard')}>
            Patient Management System
          </h1>
        </div>
        
        <nav className="header-nav">
          <button 
            className="nav-button"
            onClick={() => navigate('/dashboard')}
          >
            Dashboard
          </button>
          <button 
            className="nav-button"
            onClick={() => navigate('/patients')}
          >
            Patients
          </button>
        </nav>

        <div className="header-right">
          <span className="user-info">Welcome, {currentUser}</span>
          <button className="logout-button" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>
    </header>
  );
};