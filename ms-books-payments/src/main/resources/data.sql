-- 1. Insertar el Pago (Padre)
INSERT INTO purchases (user_id, amount, payment_method, successful_payment)
VALUES ('user_123', 100.0, 'CARD', true);

-- 2. Insertar los detalles
-- Cambiamos NAME_BOOK por el nombre que Hibernate genera (probablemente name_book)
INSERT INTO purchases_details (transaction_id, book_id, book_name, value_paid)
VALUES (1, 'BOOK_99', 'El Quijote', 50.0);

INSERT INTO purchases_details (transaction_id, book_id, book_name, value_paid)
VALUES (1, 'BOOK_44', 'Clean Code', 50.0);