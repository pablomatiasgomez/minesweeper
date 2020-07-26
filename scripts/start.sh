# cd to project root
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path/.."

PID=$(pgrep -f "minesweeper")
if [[ $PID ]]; then
	echo "Killing process $PID"
	kill "$PID"
fi
echo "Starting app!"
nohup /usr/bin/java -jar target/minesweeper-jar-with-dependencies.jar >> logs/stdout.log 2>&1 &
