reservation_id   SERIAL PRIMARY KEY
guest_name       VARCHAR(250) NOT NULL
room_number      INT NOT NULL
contact_number   VARCHAR(250) NOT NULL
reservation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP

->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->

----------------------------------------------------------------------------------------------------------------------------

                                                 Table "public.reservation"
      Column      |            Type             | Collation | Nullable |                       Default                  
------------------+-----------------------------+-----------+----------+-----------------------------------------------------
 reservation_id   | integer                     |           | not null | nextval('reservation_reservation_id_seq'::regclass)
 guest_name       | character varying(250)      |           | not null |
 room_number      | integer                     |           | not null |
 contact_number   | character varying(250)      |           | not null |
 reservation_time | timestamp without time zone |           |          | CURRENT_TIMESTAMP
Indexes:
    "reservation_pkey" PRIMARY KEY, btree (reservation_id)

->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
guest_name → Omkar

room_number → 1

contact_number → 9309283954

reservation_id → auto-generated

reservation_time → defaults to current timestamp
