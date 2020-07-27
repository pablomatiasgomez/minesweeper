# cd to project root
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path/.."

echo "Starting app!"
/usr/bin/java -jar target/minesweeper-jar-with-dependencies.jar
