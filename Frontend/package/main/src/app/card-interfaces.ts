export interface UowId {
  name?: string;
  card_type?: string;
  id?: number;
  student_number?: string;
  barcode_number?: string;
}

// Updated interface for Drivers License
export interface NSWDriversLicense {
  id?: number;
  card_type?: string;
  name?: string;
  expiry_date?: string;
  dob?: string;
  address?: string;
  licence_no?: string;
  licence_class?: string;
  card_number?: string;
}

export interface NSWPhotoCard {
  id?: number;
  card_type?: string;
  name?: string;
  address?: string;
  pc_number?: string;
  card_number?: string;
  dob?: string;
  expiry_date?: string;
}

export interface NSWDriversLicenseProvisional {
  id?: number;
  card_type?: string;
  expiry_date?: string;
  dob?: string;
  address?: string;
  licence_no?: string;
  licence_class?: string;
  licence_conditions?: string;
  card_number?: string;
}

export interface Medicare {
  id?: number;
  card_type?: string;
  medicare_number?: string;
  cardholder_1?: string;
  expiry_date?: string;
}
