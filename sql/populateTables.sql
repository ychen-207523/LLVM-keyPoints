-- Insert Drivers
INSERT INTO Drivers (name, status, ID)
VALUES
    ('Linda Garcia', 'S', '133-TLH'),
    ('Mary Jones', 'V', '685-FVS'),
    ('Linda Garcia', 'V', '882-DEF'),
    ('Robert Martinez', 'V', '240-AFN'),
    ('William Smith', 'S', '786-GJE'),
    ('James Smith', 'S', '444-YBT'),
    ('James Brown', 'V', '7729119111'),
    ('Robert Johnson', 'E', '266399121'),
    ('Elizabeth Rodriguez', 'E', '366399121'),
    ('John Williams', 'E', '466399121'),
    ('Mary Johnson', 'S', '122765234'),
    ('Robert Miller', 'V', '9194789124');

-- Insert Parking Lots
INSERT INTO ParkingLots (name, address)
VALUES
    ('Lot 9', '200 Main St, Riverside, FL, 56789'),
    ('Lot 16', '476 Cedar St, Lakeview, CA, 56789'),
    ('Lot 24', '818 Pine Ave, Springfield, TX, 23456');
    
-- Insert Zone   
    INSERT INTO Zones (id, lotName)
VALUES
    ('A',   'Lot 9'),
    ('AS',  'Lot 9'),
    ('BS',   'Lot 16'),
    ('B',   'Lot 16'),
    ('C',   'Lot 24'),
    ('CS',  'Lot 24'),
    ('V',   'Lot 9');
    
-- Insert Space   
INSERT INTO Spaces (number, type, status, zoneID, lotName)
VALUES
    (1,          'regular', 	0, 'A',  'Lot 9'),
    (1,          'electric',    1, 'AS', 'Lot 9'),
    (3,          'handicap',    1, 'B',  'Lot 16'),
    (400,        'regular',     1, 'B',  'Lot 24'),
    (500,        'electric',    1, 'C',  'Lot 24'),
    (123,        'handicap',    0, 'CS', 'Lot 24'),
    (1234567890, 'regular',     0, 'V',  'Lot 9');

-- Insert Vehicles
INSERT INTO Vehicles (carLicenseNumber, Model, Color, Manufacturer, Year)
VALUES
    ('SBF', 'GT-R-Nismo', 'Pearl White TriCoat', 'Nissan', 2024),
    ('Clay1', 'Mary Smith', 'Mary Miller', 'Tesla', 2023),
    ('Hicks1', 'Robert Smith', 'Jennifer Williams', 'BMW', 2024),
    ('Garcia1', 'Continental GT Speed', 'Jennifer Garcia', 'Bentley', 2024),
    ('CRICKET', 'William Miller', 'Sonic Gray Pearl', 'Honda', 2024),
    ('PROFX', 'Taycan Sport Turismo', 'Linda Williams', 'Porsche', 2024),
    ('VAN-9910', 'Linda Miller', 'Jennifer Williams', 'Porsche', 2023);

-- Insert Permits
INSERT INTO Permits (permitID, permitType, zoneID, associatedID, carLicenseNum, spaceType, startDate, expirationDate, expirationTime)
VALUES
    ('VSBF1C', 'Commuter', 'V', '7729119111', 'SBF', 'Regular', '2023-01-01', '2024-01-01', '06:00:00'),
    ('EJC1R', 'Residential', 'A', '266399121', 'Clay1', 'Electric', '2010-01-01', '2030-01-01', '06:00:00'),
    ('EJH2C', 'Commuter', 'A', '366399121', 'Hicks1', 'Regular', '2023-01-01', '2024-01-01', '06:00:00'),
    ('EIG3C', 'Commuter', 'A', '466399121', 'Garcia1', 'Regular', '2023-01-01', '2024-01-01', '06:00:00'),
    ('SST1R', 'Residential', 'AS', '122765234', 'CRICKET', 'Regular', '2022-01-01', '2023-09-30', '06:00:00'),
    ('VCX1SE', 'Residential', 'V', '9194789124', 'PROFX', 'Handicap', '2023-01-01', '2023-11-15', '06:00:00');

-- Insert Citations
INSERT INTO Citations (citationNum, citationDate, citationTime, paymentStatus, lotName, category, fee, licenseNum)
VALUES
    (1, '2021-10-11', '08:00:00', 'PAID', 'Lot 9', 'No permits', 40, 'VAN-9910'),
    (2, '2023-10-01', '08:00:00', 'DUE', 'Lot 16', 'Invalid permits', 30, 'CRICKET');