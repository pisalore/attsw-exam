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


