CREATE TABLE permissions(
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    publish TINYINT(1) NOT NULL DEFAULT 1, -- publish là cột để xác định xem permission có được xuất bản hay không. Giá trị 1 đại diện cho "xuất bản" và giá trị 0 đại diện cho "không xuất bản".
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);