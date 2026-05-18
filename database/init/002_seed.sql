INSERT INTO categories (name, sort_order) VALUES
  ('快递代取', 10),
  ('学习辅导', 20),
  ('二手交易', 30),
  ('活动组队', 40),
  ('其他', 50)
ON CONFLICT (name) DO NOTHING;

