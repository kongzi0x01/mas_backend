-- 菜单SQL
INSERT INTO `sys_menu_new` (`parent_id`, `name`, `path`, `redirect`, `component`, `title`, `is_link`, `is_hide`, `is_keep_alive`, `is_affix`, `is_iframe`, `icon`, `roles`, `order_sort`, `disabled`)
    VALUES (0, 'masorder', '/masorder', NULL, 'masOrder/index', '订单', NULL, 0, 1, 0, 0, 'iconfont icon-caidan', 'admin', 4, 1);

