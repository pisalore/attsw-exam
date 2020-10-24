CREATE TABLE museums(
   ID UUID  NOT NULL,
   museum_name TEXT NOT NULL,
   number_of_occupied_rooms INT NOT NULL,
   number_of_rooms INT NOT NULL,
   UNIQUE (museum_name),
   PRIMARY KEY (ID)

);

CREATE TABLE exhibitions(
   ID UUID  NOT NULL,
   museum_id UUID NOT NULL references museums(ID),
   exhibition_name TEXT NOT NULL,
   total_seats INT NOT NULL,
   booked_seats INT NOT NULL,
   UNIQUE (exhibition_name),
   PRIMARY KEY (ID)

);


INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)
VALUES ( 'b433da18-ba5a-4b86-92af-ba11be6314e7' , 'museum1_test', 0, 10);

INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)
VALUES ( '94fe3013-9ebb-432e-ab55-e612dc797851' , 'museum2_test', 0, 10);

INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)
VALUES ('49d13e51-2277-4911-929f-c9c067e2e8b4', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition1_test', 0, 100);

INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)
VALUES ('b2cb1474-24ff-41eb-a8d7-963f32f6822d', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition2_test', 0, 100);

