# Advent of Code 2024 in Java with Quarkus

My implementation of the AoC 2024 solutions in Java. Advent of code is an advent calendar made out of programming puzzles. I'm not really a competitive programmer and my goal is just to have fun and explore some of the Java features while I do it. 

This project uses Java 21 with no external dependencies for the actual algorithm implementations, but JUnit is used for the tests, Quarkus for the Rest API and Maven as a build system.

If you want to learn more about AoC, please visit its website: <https://adventofcode.com/>.
 
If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Currently implemented solutions

| Day | Part 1 | Part 2 |
|-----|--------|--------|
| 1   | ✅      | ✅    |
| 2   | ✅      | ✅    |
| 3   | ✅      | ✅    |
| 4   | ✅      | ✅    |
| 5   | ✅      | ✅    |
| 6   | ✅      | ✅    |
| 7   | ✅      | ✅    |
| 8   | ✅      | ✅    |
| 9   | ✅      | ✅    |
| 10  | ✅      | ✅    |
| 11  | ✅      | ✅    |
| 12  | ✅      | ❌    |
| 13  | ✅      | ✅    |
| 14  | ✅      | ✅    |
| 15  | ✅      | ✅    |
| 16  | ✅      | ✅    |
| 17  | ➖      | ➖    |
| 18  | ➖      | ➖    |
| 19  | ➖      | ➖    |
| 20  | ➖      | ➖    |
| 21  | ➖      | ➖    |
| 22  | ➖      | ➖    |
| 23  | ➖      | ➖    |
| 24  | ➖      | ➖    |
| 25  | ➖      | ➖    |

✅ - Solution implemented
❌ - Solution not implemented
➖ - Puzzle not available yet
## REST API

The project exposes a REST API allowing you to upload a file containing your input for the puzzle and get back the answer:

| Method | Endpoint            | ContentType         |
|--------|---------------------|---------------------|
| PUT    | /solve/{day}/{part} | multipart/form-data |

Day is a number between 1 and 25, representing the day of the challenge. Part is either 1 or 2, representing each part of the puzzle. The input file must be sent as part of the form, with the key `input`.

For day 14, part 2, there are a few query parameters that control how the search for the solution is handled.

| Parameter        | Type                    | Description         |
|------------------|-------------------------|---------------------|
| day_14_start     | Integer                 | Define where the iteration begins |
| day_14_end       | Integer                 | Define where the iteration ends |
| day_14_visualize | Boolean (true or false) | If true, will generate visualizations of each iteration instead of returning the solution |

The last iteration (day_14_end) is cached, so the most efficient way to run the solution is to pass the same value that was used as day_14_end on the previous request, as day_14_start on the next request. Mathematically, using day_14_start=0/day_14_end=10403 should guarantee finding the solution.


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```