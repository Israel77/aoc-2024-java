# Advent of Code 2024 in Java with Quarkus

My implementation of the AoC 2024 solutions in Java. Advent of code is an advent calendar made out of programming puzzles. I'm not really a competitive programmer and my goal is just to have fun and explore some of the Java features while I do it. 

This project uses Java 21 with no external dependencies for the algorithm implementations, but JUnit is used for the tests and Quarkus for the Rest API.

If you want to learn more about AoC, please visit its website: <https://adventofcode.com/>
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
| 8   | ❌      | ❌    |
| 9   | ❌      | ❌    |
| 11  | ➖      | ➖    |
| 12  | ➖      | ➖    |
| 13  | ➖      | ➖    |
| 14  | ➖      | ➖    |
| 15  | ➖      | ➖    |
| 16  | ➖      | ➖    |
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

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```