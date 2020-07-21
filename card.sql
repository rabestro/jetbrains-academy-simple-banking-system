CREATE TABLE card (
    id      INTEGER PRIMARY KEY,
    number  TEXT NOT NULL UNIQUE CHECK ( length(number) = 16 ),
    pin     TEXT NOT NULL CHECK ( length(pin) = 4 ),
    balance INTEGER NOT NULL DEFAULT 0 CHECK ( balance >= 0 )
);
