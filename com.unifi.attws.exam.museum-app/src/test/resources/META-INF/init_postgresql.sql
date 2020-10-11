CREATE TABLE museums(
   ID UUID  NOT NULL,
   museum_name TEXT NOT NULL,
   number_of_occupied_rooms INT NOT NULL,
   number_of_rooms INT NOT NULL,
   UNIQUE (museum_name),
   PRIMARY KEY (ID)

);

INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)
VALUES ( 'b433da18-ba5a-4b86-92af-ba11be6314e7' , 'test1', 0, 10);

INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)
VALUES ( '94fe3013-9ebb-432e-ab55-e612dc797851' , 'test2', 0, 10);
