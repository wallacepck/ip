# Mana User Guide

// Product screenshot goes here

Mana frees your mind of having to remember things you need to do. It's,
- text-based
- easy to learn 
- ~~FAST~~ SUPER FAST to use

## Adding tasks
Adds a task to the tasklist, the tasks can be a `todo`, `deadline`, or`event`
### Usage:
* Todo: \
  `todo TITLE`


* Deadline: \
  `deadline TITLE --by DATE`


* Event: \
  `event TITLE --from START_DATE --to END_DATE`

Date format is strictly `dd-MM-yyyy` 

### Examples: 
* Create a todo to do homework: \
  `todo do homework`


* Create a deadline to return a book: \
  `deadline return book --by 21-02-2025`


* Create an event for project work: \
  `event project work --from 21-02-2025 --to 25-02-2025`

## Deleting tasks
Delete a task from the list

### Usage:
`delete INDEX`

whereby `INDEX` is the task's index on the list.  


## Listing tasks
List all tasks on the list

### Usage:
`list`


## Finding tasks
Finds tasks whose title contains the given text

### Usage:
`find TEXT`

### Examples:
* Find tasks with "book" in title:\
`find book`


* Find tasks with "cheese toast" in title:\
`find cheese toast`


## Marking tasks as Done/Undone
Mark a task as done/undone
### Usage:

`done INDEX`

`undone INDEX`

whereby `INDEX` is the task's index on the list.

## Saving & Loading
Saving and loading is done automatically to a file located at `.\data\tasks.json`.
There is no input required from the user. 

## Loading from other files
Load the tasklist from a previously saved file located in the `.\data` directory.

### Usage:
`load FILENAME` 

`FILENAME` includes the `.json` extension. Loading files from other directories is currently not supported.

### Example:
`load test.json`


## Exiting
Exit the program. Program will gracefully shut down shortly after using the command.

### Usage:
`bye`
