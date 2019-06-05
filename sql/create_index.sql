CREATE INDEX index_customer_id ON Customer USING btree(id);

CREATE INDEX index_customer_fname ON Customer USING BTREE (fname);

CREATE INDEX index_customer_lname ON Customer USING BTREE (lname);

CREATE INDEX index_customer_gtype ON Customer USING BTREE (gtype);

CREATE INDEX index_customer_dob ON Customer USING BTREE(dob);

CREATE INDEX index_customer_address ON Customer USING BTREE(address);

CREATE INDEX index_customer_phone ON Customer USING BTREE (phone);

CREATE INDEX index_customer_zipcode ON Customer USING BTREE (zipcode);

/* index for customer */

CREATE INDEX index_pilot_id ON Pilot USING BTREE (id);

CREATE INDEX index_pilot_fullname ON Pilot USING BTREE (fullname);

CREATE INDEX index_pilot_nationality ON Pilot USING BTREE (nationality);

/* index for pilot */

CREATE INDEX index_Flight_fnum ON Flight USING BTREE (fnum);

CREATE INDEX index_Flight_cost ON Flight USING BTREE (cost);

CREATE INDEX index_Flight_num_sold ON Flight USING BTREE (num_sold);

CREATE INDEX index_Flight_num_stops ON Flight USING BTREE (num_stops);

CREATE INDEX index_Flight_actual_departure_date ON Flight USING BTREE (actual_departure_date);

CREATE INDEX index_Flight_actual_arrival_date ON Flight USING BTREE (actual_arrival_date);

CREATE INDEX index_Flight_arrival_airport ON Flight USING BTREE (arrival_airport);

CREATE INDEX index_Flight_departure_airport ON Flight USING BTREE (departure_airport);

/* index for Flight */

CREATE INDEX index_Plane_id ON Plane USING BTREE (id);

CREATE INDEX index_Plane_make ON Plane USING BTREE (make);

CREATE INDEX index_Plane_model ON Plane USING BTREE (model);

CREATE INDEX index_Plane_age ON Plane USING BTREE (age);

CREATE INDEX index_Plane_seats ON Plane USING BTREE (seats);

/* index for Plane */

CREATE INDEX index_Technician_id ON Technician USING BTREE (id);

CREATE INDEX index_Technician_full_name ON Technician USING BTREE (full_name);

/* index for Technician */

CREATE INDEX index_Reservation_rnum ON Reservation USING BTREE (rnum);

CREATE INDEX index_Reservation_cid ON Reservation USING BTREE (cid);

CREATE INDEX index_Reservation_fid ON Reservation USING BTREE (fid);

/* index for Reservation */

CREATE INDEX index_FlightInfo_fiid ON FlightInfo USING BTREE (fiid);

CREATE INDEX index_FlightInfo_flight_id ON FlightInfo USING BTREE (flight_id);

CREATE INDEX index_FlightInfo_pilot_id ON FlightInfo USING BTREE (pilot_id);

CREATE INDEX index_FlightInfo_plane_id ON FlightInfo USING BTREE (plane_id);

/* Index for FlightInfo */

CREATE INDEX index_Repairs_rid ON Repairs USING BTREE (rid);

CREATE INDEX index_Repairs_repair_date ON Repairs USING BTREE (repair_date);

CREATE INDEX index_Repairs_repair_code ON Repairs USING BTREE (repair_code);

CREATE INDEX index_Repairs_pilot_id ON Repairs USING BTREE (pilot_id);

CREATE INDEX index_Repairs_plane_id ON Repairs USING BTREE (plane_id);

CREATE INDEX index_Repairs_technician_id ON Repairs USING BTREE (technician_id);

/* Index for Repairs */

CREATE INDEX index_Schedule_id ON Schedule USING BTREE (id);

CREATE INDEX index_Schedule_flightNum ON Schedule USING BTREE (flightNum);

CREATE INDEX index_Schedule_departure_time ON Schedule USING BTREE (departure_time);

CREATE INDEX index_Schedule_arrival_time ON Schedule USING BTREE (arrival_time);

/* Index for Schedule */
