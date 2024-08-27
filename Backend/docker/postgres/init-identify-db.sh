set -e

#export PG_PASSWD=$(cat /run/secrets/postgres_pass)

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER identify;
	CREATE DATABASE identify;
	GRANT ALL PRIVILEGES ON DATABASE identify TO identify;
	ALTER USER identify PASSWORD 'Dev123!';
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "identify" <<-EOSQL
	GRANT ALL ON SCHEMA public TO identify;
EOSQL

psql -v ON_ERROR_STOP=1 --username "identify" --dbname "identify" <<-EOSQL
	CREATE SCHEMA jobrunr;
EOSQL
