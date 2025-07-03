-- SQLite版本的数据库初始化脚本

CREATE TABLE IF NOT EXISTS `todo_task` (
  `id` INTEGER PRIMARY KEY,
  `user_id` INTEGER NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `describe` TEXT,
  `star` INTEGER NOT NULL DEFAULT 0, -- 0：未标星，1：标星
  `status` INTEGER NOT NULL, -- 0：未完成，1：完成
  `create_at` TEXT DEFAULT (datetime('now')),
  `update_at` TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS `todo_user` (
  `id` INTEGER PRIMARY KEY,
  `wechat_id` VARCHAR(100),
  `username` VARCHAR(100), -- 用户名称
  `password` VARCHAR(50), -- 用户密码
  `create_at` TEXT DEFAULT (datetime('now')),
  `update_at` TEXT DEFAULT (datetime('now'))
);
