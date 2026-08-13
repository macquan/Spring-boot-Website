CREATE TABLE user_catalogue_permission(
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_catalogue_id BIGINT UNSIGNED NOT NULL,
    permission_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_user_catalogue FOREIGN KEY (user_catalogue_id) REFERENCES user_catalogues(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE
    -- On DELETE CASCADE: Khi một bản ghi trong bảng cha (user_catalogues hoặc permissions) bị xóa, tất cả các bản ghi liên quan trong bảng con (user_catalogue_permission) cũng sẽ bị xóa tự động.
    -- On UPDATE CASCADE: Khi một bản ghi trong bảng cha (user_catalogues hoặc permissions) bị cập nhật, tất cả các bản ghi liên quan trong bảng con (user_catalogue_permission) cũng sẽ được cập nhật tự động.
);