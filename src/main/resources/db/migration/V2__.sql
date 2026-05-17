CREATE TABLE payout_requests
(
    payout_id      UUID         NOT NULL,
    amount         DECIMAL      NOT NULL,
    card_number    VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status         VARCHAR(255) NOT NULL,
    fundraising_id UUID         NOT NULL,
    CONSTRAINT pk_payout_requests PRIMARY KEY (payout_id)
);

ALTER TABLE payout_requests
    ADD CONSTRAINT FK_PAYOUT_REQUESTS_ON_FUNDRAISING FOREIGN KEY (fundraising_id) REFERENCES fundraising (fundraising_id);

ALTER TABLE donations
ALTER
COLUMN amount TYPE DECIMAL USING (amount::DECIMAL);

ALTER TABLE fundraising
ALTER
COLUMN goal_amount TYPE DECIMAL USING (goal_amount::DECIMAL);