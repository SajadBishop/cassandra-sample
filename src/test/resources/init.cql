CREATE KEYSPACE if not exists testSpace
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
--
-- use testSpace;
--
-- create type peer (
--     peer_type text,
--     peer_id text
-- );