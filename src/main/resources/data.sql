INSERT INTO account(username, full_name, email, phone, address, password, role)
VALUES
    ("admin", "Nguyen Van Tho", "tho@gmail.com", "0987098765", "Dan Phuong, Ha Noi", "$2a$10$XuA6fVMq0GXoZ6qiS6YZkukHouEJp.brS5aO4tTnj8WHnA7F.QGFK", "ADMIN");

INSERT INTO product(name,material, size, price, discount, image, rating, type)
VALUES
    ("Ghế phòng ngủ", "Gỗ thông", "123 x 340 x 250", 1000000, 150,  "/assets/images/product-1.png", 4,  "CHAIR"),
    ("Giuong", "Gỗ mun", "123 x 340 x 250", 2000000, 550,  "/assets/images/product-2.png", 4,  "BED");


-- #google:
-- Client ID: 98942692648-alc65fm7g5ge8r1kpt0mrdb086mltok8.apps.googleusercontent.com
-- Client secret: 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