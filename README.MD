# Experiment Instructions

These instructions describe the full procedure for running the controlled experiment on how text editors influence code‑refactoring quality.

---

## 1. Overview
This study examines whether the type of text editor used during refactoring affects code quality, correctness, maintainability, and task completion time. Participants will perform multiple refactoring tasks using a specific assigned editor.

You **must follow the assigned editor condition strictly**.

---

## 2. Before You Begin
### Step 1 — Complete the Pre‑study Questionnaire page
Before performing any tasks, fill out the **Pre‑study Questionnaire** page. Your answers help us:
- measure experience level,
- evaluate editor familiarity,
- control for confounding factors.

### Step 2 — Receive Your Assigned Editor Condition
You will be randomly assigned to **one** of the following conditions:

1. **Plain text editor **Vi** — minimal assistance, no GUI features.
2. **Lightweight editor** — syntax highlighting, basic search, limited tooling.

You must use **only the tools allowed** in your assigned condition. External IDEs or refactoring assistants are **not permitted**.

### Step 3 — Complete the Short Tutorial
A short tutorial will familiarize you with:
- the editor assigned to you,
- basic navigation and commands,
- how to open/run/view Java code.

This ensures that lack of editor familiarity does not bias results.

---

## 3. Experimental Tasks
Your tasks will be to refactor a Java program using the assigned editor. 

### Task Types
Tasks may involve:
- Removing duplication
- Extracting or reorganizing methods
- Improving readability or structure
- Fixing minor logic issues
- Eliminating dead code
- Enhancing maintainability such as naming, cohesion, separation of concerns

### Time Limits
Each task has a recommended time window of **25 minutes**. Total session duration is approx. **70 minutes**.

### What You Must Record
For each task, record:
- Start and end time
- Any difficulties or notable decisions

After each task, **save your code** and **commit your changes** to Git or send it via email to tomasz.korcz@student.put.poznan.pl.

---

## 4. Allowed and Forbidden Tools
### Allowed
- Your assigned editor
- Built‑in search (text or regex)
- Simple command‑line tools (javac, java)
- Notes taken on paper or plain text

### Forbidden
- Any IDE with automated refactorings
- AI‑assisted tools (ChatGPT, Copilot, etc.)
- External code‑analysis tools
- Plugins or extensions that provide refactoring support

---

## 5. After Completing All Tasks
### Step 1 — Complete the Post‑task Questionnaire
You will be asked about:
- perceived difficulty of each task,
- editor usability,
- perceived effect on performance,
- whether you introduced or detected defects,
- mental workload

This information is essential for interpreting the performance data.

### Step 2 — Optional Debrief
You may request a short debrief explaining:
- the study’s hypotheses,
- why the editor conditions differ,
- the expected outcomes.

---

## 6. Data Collected
We collect:
- Your questionnaire responses
- Task completion times
- Refactored code
- Any identified bugs or regressions
- Metadata such as editor condition and familiarity

All data is anonymized before analysis.

---

Thank you for participating in this experiment!
