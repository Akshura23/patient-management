import { Patient } from '../types';

export const mockPatients: Patient[] = [
  {
    uid: 1,
    firstName: 'John',
    lastName: 'Doe',
    birthday: '1985-03-15',
    mobileNo: '+1-555-0123',
    email: 'john.doe@example.com',
    address: '123 Main Street, Anytown, NY 12345',
    gender: 'MALE',
    emergencyContactName: 'Jane Doe',
    emergencyContactPhone: '+1-555-0124',
    insuranceInfo: 'BlueCross BlueShield - Policy #BC12345678',
    createdBy: 'admin',
    createdDate: '2025-01-15T10:30:00',
    lastModifiedBy: 'admin',
    lastModifiedDate: '2025-01-15T10:30:00'
  },
  {
    uid: 2,
    firstName: 'Emma',
    lastName: 'Johnson',
    birthday: '1992-07-22',
    mobileNo: '+1-555-0456',
    email: 'emma.johnson@example.com',
    address: '456 Oak Avenue, Somewhere, CA 90210',
    gender: 'FEMALE',
    emergencyContactName: 'Michael Johnson',
    emergencyContactPhone: '+1-555-0457',
    insuranceInfo: 'Aetna - Policy #AE87654321',
    createdBy: 'admin',
    createdDate: '2025-01-20T14:15:00',
    lastModifiedBy: 'admin',
    lastModifiedDate: '2025-01-20T14:15:00'
  },
  {
    uid: 3,
    firstName: 'Robert',
    lastName: 'Smith',
    birthday: '1978-11-08',
    mobileNo: '+1-555-0789',
    email: 'robert.smith@example.com',
    address: '789 Pine Road, Elsewhere, TX 73301',
    gender: 'MALE',
    emergencyContactName: 'Sarah Smith',
    emergencyContactPhone: '+1-555-0790',
    insuranceInfo: 'Cigna - Policy #CG11223344',
    createdBy: 'admin',
    createdDate: '2025-02-01T09:45:00',
    lastModifiedBy: 'admin',
    lastModifiedDate: '2025-02-01T09:45:00'
  },
  {
    uid: 4,
    firstName: 'Maria',
    lastName: 'Garcia',
    birthday: '1995-05-12',
    mobileNo: '+1-555-0321',
    email: 'maria.garcia@example.com',
    address: '321 Elm Street, Newcity, FL 33101',
    gender: 'FEMALE',
    emergencyContactName: 'Carlos Garcia',
    emergencyContactPhone: '+1-555-0322',
    insuranceInfo: 'UnitedHealthcare - Policy #UH55667788',
    createdBy: 'admin',
    createdDate: '2025-02-10T16:20:00',
    lastModifiedBy: 'admin',
    lastModifiedDate: '2025-02-10T16:20:00'
  },
  {
    uid: 5,
    firstName: 'David',
    lastName: 'Wilson',
    birthday: '1989-09-30',
    mobileNo: '+1-555-0654',
    email: 'david.wilson@example.com',
    address: '654 Maple Drive, Oldtown, WA 98101',
    gender: 'MALE',
    emergencyContactName: 'Lisa Wilson',
    emergencyContactPhone: '+1-555-0655',
    insuranceInfo: 'Kaiser Permanente - Policy #KP99887766',
    createdBy: 'admin',
    createdDate: '2025-02-15T11:10:00',
    lastModifiedBy: 'admin',
    lastModifiedDate: '2025-02-15T11:10:00'
  }
];

let mockPatientData = [...mockPatients];
let nextId = 6;

export const mockApiService = {
  // Health check
  health: () => Promise.resolve({ data: { status: 'UP', message: 'Patient Management API is running (Demo Mode)' } }),

  // Authentication
  login: (loginData: any) => Promise.resolve({ 
    data: { success: true, message: 'Login successful', username: loginData.username } 
  }),

  // User management
  createAdmin: (userData: any) => Promise.resolve({ data: 'Admin user created successfully' }),
  getUserCount: () => Promise.resolve({ data: 1 }),

  // Patient management
  getAllPatients: () => Promise.resolve({ data: mockPatientData }),
  
  getPatientById: (id: number) => {
    const patient = mockPatientData.find(p => p.uid === id);
    if (patient) {
      return Promise.resolve({ data: patient });
    }
    return Promise.reject(new Error('Patient not found'));
  },
  
  createPatient: (patient: any) => {
    const newPatient = {
      ...patient,
      uid: nextId++,
      createdBy: 'admin',
      createdDate: new Date().toISOString(),
      lastModifiedBy: 'admin',
      lastModifiedDate: new Date().toISOString()
    };
    mockPatientData.push(newPatient);
    return Promise.resolve({ data: newPatient });
  },
  
  updatePatient: (id: number, patient: any) => {
    const index = mockPatientData.findIndex(p => p.uid === id);
    if (index !== -1) {
      const updatedPatient = {
        ...patient,
        uid: id,
        createdBy: mockPatientData[index].createdBy,
        createdDate: mockPatientData[index].createdDate,
        lastModifiedBy: 'admin',
        lastModifiedDate: new Date().toISOString()
      };
      mockPatientData[index] = updatedPatient;
      return Promise.resolve({ data: updatedPatient });
    }
    return Promise.reject(new Error('Patient not found'));
  },
  
  deletePatient: (id: number) => {
    const index = mockPatientData.findIndex(p => p.uid === id);
    if (index !== -1) {
      mockPatientData.splice(index, 1);
      return Promise.resolve({ data: null });
    }
    return Promise.reject(new Error('Patient not found'));
  }
};