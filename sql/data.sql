USE shiyan3;

INSERT INTO users (username, password, role, nickname, phone, enabled)
VALUES
('admin', 'admin123', 'admin', '系统管理员', '13800000000', 1),
('user1', 'user123', 'user', '普通用户', '13900000000', 1)
ON DUPLICATE KEY UPDATE username = VALUES(username);

INSERT INTO houses (title, community, address, region, layout, area, price, image_url, description, status, reject_reason, created_by)
VALUES
('地铁口精装两居', '阳光花园', '幸福路 88 号', '朝阳', '2室1厅', 89.50, 320.00, 'https://picsum.photos/500/300?random=1', '采光好，近地铁，拎包入住', 'APPROVED', NULL, 1),
('学区房三居室', '书香雅苑', '文昌路 100 号', '海淀', '3室2厅', 121.00, 560.00, 'https://picsum.photos/500/300?random=2', '满五唯一，南北通透', 'PENDING', NULL, 1),
('江景大平层', '滨江壹号', '滨江大道 18 号', '浦东', '4室2厅', 188.00, 1180.00, 'https://picsum.photos/500/300?random=3', '一线江景，豪华装修', 'REJECTED', '证件信息不完整', 1)
ON DUPLICATE KEY UPDATE title = VALUES(title);
