create database CarConnect;
Use CarConnect;
create table Customer (
    CustomerID int primary key,
    FirstName varchar(50),
    LastName varchar(50),
    Email varchar(100),
    PhoneNumber varchar(10),
    Address varchar(200),
    Username varchar(50) unique,
    Password varchar(50), 
    RegistrationDate date
);

create table Vehicle (
    VehicleID int primary key,
    Model varchar(50),
    Make varchar(50),
    Year int,
    Color varchar(25),
    RegistrationNumber varchar(20) unique,
    Availability boolean,
    DailyRate decimal(10, 2)
);

create table Reservation (
    ReservationID int primary key,
    CustomerID int,
    VehicleID int,
    StartDate datetime,
    EndDate datetime,
    TotalCost decimal(10, 2),
    Status varchar(20),
    foreign key (CustomerID) references Customer(CustomerID),
    foreign key (VehicleID) references Vehicle(VehicleID)
);

CREATE TABLE Admin (
    AdminID int primary key,
    FirstName varchar(50),
    LastName varchar(50),
    Email varchar(100),
    PhoneNumber varchar(10),
    Username varchar(50) unique,
    Password varchar(50), 
    Role varchar(30),
    JoinDate date
);


insert into Customer values
(1, 'Dharshini', 'Suresh', 'dharshusuresh@gmail.com', 1234567890, 'Pondicherry', 'customeraccountsDharshinis', 'dharshus@12', '2023-08-15'),
(2, 'Aishwarya', 'Vasudev', 'aishvasy@gmail.com', 9876543210, 'chennai', 'Aishuvasudev', 'aishvd@92', '2024-01-15'),
(3, 'Veera', 'Kumar', 'veeruxxx@gmail.com', 1112223334, 'Kanyakumari', 'veerask', 'veeru@56', '2024-03-02'),
(4, 'Bhuvan', 'Williams', 'bhuvan.williams@gmail.com', 4445556667, 'Villupuram', 'bhuvanw', 'wbhuvan@56', '2023-11-04'),
(5, 'Gopal', 'Samy', 'Samy.gopal@gmail.com', 7778889990, 'Kanyakumari', 'gopasls', 'gopal@77', '2024-03-05');


insert into Vehicle values
(1, 'Bronco', 'Ford', 2022, 'Red', 'ABC123', 1, 500.00),
(2, 'Accord', 'Honda', 2023, 'Blue', 'DEF456', 1, 600.00),
(3, 'Blazer', 'Chevrolet', 2024, 'Silver', 'GHI789', 0, 550.00),
(4, 'Camry', 'Toyota', 2020, 'Black', 'JKL012', 1, 650.00),
(5, 'Altima', 'Nissan', 2022, 'White', 'MNO345', 1, 700.00),
(6, 'Altima', 'Nissan', 2021, 'Blue', 'MJK485', 0, 700.00);

insert into Reservation values
(1, 1, 2, '2024-03-03 08:00:00', '2024-03-07 17:00:00', 2750.00, 'Confirmed'),
(2, 2, 4, '2024-03-01 10:00:00', '2024-03-05 15:00:00', 3250.00, 'Confirmed'),
(3, 3, 1, '2024-02-28 09:00:00', '2024-03-02 16:00:00', 2400.00, 'Confirmed'),
(4, 4, 5, '2024-02-16 11:00:00', '2024-02-28 14:00:00', 2600.00, 'Confirmed'),
(5, 5, 3, '2024-02-24 13:00:00', '2024-03-01 12:00:00', 3500.00, 'Confirmed');


insert into Admin values
(1, 'Admin', 'One', 'admin1@example.com', '1234567890', 'admin1', 'admin123', 'manager', '2024-01-05'),
(2, 'Admin', 'Two', 'admin2@example.com', '9876543210', 'admin2', 'admin456', 'SubAdmin', '2024-02-12'),
(3, 'Admin', 'Three', 'admin3@example.com', '1122334455', 'admin3', 'admin789', 'Admin', '2024-01-03'),
(4, 'Admin', 'Four', 'admin4@example.com', '5566778899', 'admin4', 'admin101112', 'Admin', '2024-02-14'),
(5, 'Admin', 'Five', 'admin5@example.com', '9900112233', 'admin5', 'admin131415', 'Admin', '2024-03-05');

select * from customer;
select * from vehicle;
select * from admin;
select * from reservation