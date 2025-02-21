-- Insert into users
INSERT INTO users (email, password) VALUES
                                        ('admin1@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi'),
                                        ('employee1@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi'),
                                        ('customer1@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi'),
                                        ('customer2@gmail.com', '$2a$12$RTNq7JGW68FJDTsc3bGxmeyQueGFqAOh5NByss/JyvCglxKYZQ6zi');

-- Insert into admins
INSERT INTO admins (name, surname, email, register_date, update_date, user_id) VALUES
    ('Agil', 'Azizov', 'admin1@gmail.com', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1);

-- Insert into customers
INSERT INTO customers (name, surname, email, phone, birthday, city, state, country, register_date, update_date, user_id) VALUES
        ('Akbar', 'Maharramov', 'customer1@gmail.com', '+994556765532', '2005-09-11', 'Baku', '-', 'Azerbaijan', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 3),
        ('Cavid', 'Mammadli', 'customer2@gmail.com', '+994556765532', '2005-09-11', 'Baku', '-', 'Azerbaijan', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 4);

-- Insert into hotels
INSERT INTO hotels (name, location, rating, description, number_of_rooms, available_rooms, has_pool, has_gym, has_wifi, has_restaurant, has_parking, has_spa, contact_number, email, website, check_in_time, check_out_time, city, state, country, created_at, updated_at, user_id) VALUES
('Grand Hotel', 'New York', 4.5, 'Luxury hotel in NY', 100, 90, true, true, true, true, true, true, '123-456-789', 'grand@example.com', 'www.grandhotel.com', '14:00:00', '11:00:00', 'New York', 'NY', 'USA', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1),
('Ocean View', 'Miami', 4.7, 'Beautiful ocean view hotel', 80, 75, true, true, true, true, true, false, '321-654-987', 'ocean@example.com', 'www.oceanview.com', '15:00:00', '10:00:00', 'Miami', 'FL', 'USA', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1),
('Mountain Resort', 'Denver', 4.3, 'Relaxing stay in the mountains', 60, 55, true, false, true, true, false, true, '987-123-456', 'mountain@example.com', 'www.mountainresort.com', '14:00:00', '12:00:00', 'Denver', 'CO', 'USA', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1),
('City Lights', 'Los Angeles', 4.6, 'Perfect for business and leisure', 120, 100, true, true, true, true, true, false, '789-456-123', 'citylights@example.com', 'www.citylights.com', '13:00:00', '11:00:00', 'Los Angeles', 'CA', 'USA', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1),
('Desert Oasis', 'Las Vegas', 4.8, 'Luxury and comfort in the desert', 90, 80, true, true, true, true, true, true, '456-789-321', 'desert@example.com', 'www.desertoasis.com', '16:00:00', '12:00:00', 'Las Vegas', 'NV', 'USA', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1);

-- Insert into rooms (5 rooms per hotel)
INSERT INTO rooms (room_type, room_number, price_per_night, is_available, capacity, hotel_id, user_id) VALUES
                                                                                                           -- Grand Hotel
                                                                                                           ('DELUXE', 101, 150.00, true, 2, 1, 1),
                                                                                                           ('SUITE', 102, 200.00, true, 3, 1, 1),
                                                                                                           ('STUDIO', 103, 100.00, true, 2, 1, 1),
                                                                                                           ('DELUXE', 104, 180.00, true, 3, 1, 1),
                                                                                                           ('SUITE', 105, 250.00, true, 4, 1, 1),
                                                                                                           -- Ocean View
                                                                                                           ('DELUXE', 201, 180.00, true, 2, 2, 1),
                                                                                                           ('SUITE', 202, 250.00, true, 3, 2, 1),
                                                                                                           ('STUDIO', 203, 120.00, true, 2, 2, 1),
                                                                                                           ('DELUXE', 204, 200.00, true, 3, 2, 1),
                                                                                                           ('SUITE', 205, 300.00, true, 4, 2, 1),
                                                                                                           -- Mountain Resort
                                                                                                           ('DELUXE', 301, 140.00, true, 2, 3, 1),
                                                                                                           ('SUITE', 302, 220.00, true, 3, 3, 1),
                                                                                                           ('STUDIO', 303, 90.00, true, 2, 3, 1),
                                                                                                           ('DELUXE', 304, 160.00, true, 3, 3, 1),
                                                                                                           ('SUITE', 305, 270.00, true, 4, 3, 1),
                                                                                                           -- City Lights
                                                                                                           ('DELUXE', 401, 170.00, true, 2, 4, 1),
                                                                                                           ('SUITE', 402, 260.00, true, 3, 4, 1),
                                                                                                           ('STUDIO', 403, 110.00, true, 2, 4, 1),
                                                                                                           ('DELUXE', 404, 190.00, true, 3, 4, 1),
                                                                                                           ('SUITE', 405, 280.00, true, 4, 4, 1),
                                                                                                           -- Desert Oasis
                                                                                                           ('DELUXE', 501, 200.00, true, 2, 5, 1),
                                                                                                           ('SUITE', 502, 300.00, true, 3, 5, 1),
                                                                                                           ('STUDIO', 503, 130.00, true, 2, 5, 1),
                                                                                                           ('DELUXE', 504, 220.00, true, 3, 5, 1),
                                                                                                           ('SUITE', 505, 350.00, true, 4, 5, 1);

-- Insert into reservations (5 reservations)
INSERT INTO reservations (user_id, hotel_id, room_id, check_in_date, check_out_date, number_of_guests, total_price, status) VALUES
                                                                                                                                (3, 1, 1, '2025-04-10', '2025-04-15', 2, 750.00, 'CONFIRMED'),
                                                                                                                                (3, 2, 6, '2025-05-05', '2025-05-10', 3, 1250.00, 'CONFIRMED'),
                                                                                                                                (3, 3, 11, '2025-06-01', '2025-06-07', 2, 840.00, 'CONFIRMED'),
                                                                                                                                (4, 4, 16, '2025-07-20', '2025-07-25', 2, 950.00, 'CONFIRMED'),
                                                                                                                                (4, 5, 21, '2025-08-15', '2025-08-22', 3, 1540.00, 'CONFIRMED');

-- Insert into payments (10 payments)
INSERT INTO payments (amount, status, payment_date, reservation_id) VALUES
                                                                        (750.00, 'COMPLETED', '2022-05-05 10:10:10', 1),
                                                                        (1250.00, 'COMPLETED', '2022-05-05 10:10:10', 2),
                                                                        (420.00, 'COMPLETED', '2022-05-05 10:10:10', 3),
                                                                        (420.00, 'COMPLETED', '2022-05-05 10:10:10', 3),
                                                                        (950.00, 'PARTIAL', '2022-05-05 10:10:10', 4),
                                                                        (1540.00, 'PARTIAL', '2022-05-05 10:10:10', 5),
                                                                        (770.00, 'PARTIAL', '2022-05-05 10:10:10', 1),
                                                                        (1250.00, 'PARTIAL', '2022-05-05 10:10:10', 2),
                                                                        (500.00, 'FAILED', '2022-05-05 10:10:10', 3),
                                                                        (970.00, 'FAILED', '2022-05-05 10:10:10', 4);

-- Insert into reviews (5 reviews)
INSERT INTO reviews (title, rating, comment, created_at, updated_at, hotel_id, room_id, user_id) VALUES
                                                                                                     ('Amazing Stay!', 5, 'One of the best hotels I have ever stayed at!', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 1, 1, 3),
                                                                                                     ('Great Ocean View', 4, 'Beautiful views and excellent service.', '2022-05-05 10:10:10', '2022-05-05 10:10:10', 2, 6, 4);
-- Insert into roles
INSERT INTO roles (name, admin, employee, customer) VALUES
                                                        ('ADD_ADMIN', 1, 0, 0),
                                                        ('ADD_EMPLOYEE', 1, 0, 0),
                                                        ('ADD_HOTEL', 1, 0, 0),
                                                        ('GET_ALL_HOTELS', 1, 1, 1),
                                                        ('SEARCH_HOTEL', 1, 1, 1),
                                                        ('FIND_HOTEL_BY_ID', 1, 1, 1),
                                                        ('UPDATE_HOTEL', 1, 0, 0),
                                                        ('DELETE_HOTEL', 1, 0, 0),
                                                        ('ADD_ROOM', 1, 0, 0),
                                                        ('GET_ALL_ROOMS', 1, 1, 1),
                                                        ('FIND_ROOMS_BY_HOTEL', 1, 1, 1),
                                                        ('FIND_ROOM_BY_ID', 1, 1, 1),
                                                        ('UPDATE_ROOM', 1, 0, 0),
                                                        ('DELETE_ROOM', 1, 0, 0),
                                                        ('PROCESS_PAYMENT', 0, 1, 1),
                                                        ('GET_ALL_PAYMENTS', 1, 1, 0),
                                                        ('FIND_PAYMENT_BY_ID', 1, 1, 0),
                                                        ('FIND_PAYMENTS_BY_HOTEL', 1, 1, 0),
                                                        ('ADD_RESERVATION', 0, 1, 1),
                                                        ('GET_ALL_RESERVATIONS', 1, 1, 1),
                                                        ('FIND_RESERVATION_BY_ID', 1, 1, 1),
                                                        ('FIND_RESERVATION_BY_USER', 1, 1, 1),
                                                        ('UPDATE_RESERVATION', 1, 0, 0),
                                                        ('CONFIRM_RESERVATION', 1, 1, 0),
                                                        ('COMPLETE_RESERVATION', 1, 1, 0),
                                                        ('CHECK_IN_RESERVATION', 1, 1, 0),
                                                        ('CANCEL_RESERVATION', 1, 1, 1),
                                                        ('ADD_REVIEW', 0, 1, 1),
                                                        ('GET_ALL_REVIEWS', 1, 1, 1),
                                                        ('FIND_REVIEW_BY_ID', 1, 1, 1),
                                                        ('UPDATE_REVIEW', 0, 0, 1),
                                                        ('DELETE_REVIEW', 0, 0, 1),
                                                        ('SAVE_FILE', 1, 0, 0),
                                                        ('SAVE_PICTURE', 1, 0, 0),
                                                        ('UPDATE_PICTURE', 1, 0, 0);

INSERT INTO user_roles (user_id, role_id)
select 1,id from roles where admin=1;

INSERT INTO user_roles (user_id, role_id)
select 2,id from roles where employee=1;

INSERT INTO user_roles (user_id, role_id)
select 3,id from roles where customer=1;

