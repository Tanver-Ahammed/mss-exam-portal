# Entity Documentation

This document provides an overview of all JPA entities in the MSS Exam Portal application.

## Base Entity

All domain entities extend `BaseEntity`, which provides:

| Field       | Type          | Description                             |
|-------------|---------------|-----------------------------------------|
| `id`        | Long          | Primary key, auto-generated             |
| `createdAt` | LocalDateTime | Creation timestamp                      |
| `updatedAt` | LocalDateTime | Last modification timestamp             |
| `createdBy` | User          | FK to user who created the record       |
| `updatedBy` | User          | FK to user who last modified the record |
| `deleted`   | boolean       | Soft delete flag                        |

---

## User Package

### User

Represents a system user (admin, instructor, examiner, or student).

**Table:** `USERS`

| Field          | Type    | Constraints          | Description                                      |
|----------------|---------|----------------------|--------------------------------------------------|
| `username`     | String  | NotBlank, 3-50 chars | Unique username                                  |
| `email`        | String  | NotBlank, @Email     | Unique email address                             |
| `passwordHash` | String  | NotBlank             | BCrypt hashed password                           |
| `name`         | String  | NotBlank             | name                                             |
| `nameLocal`    | String  | NotBlank             | Localized name                                   |
| `phone`        | String  | -                    | Contact phone                                    |
| `role`         | Role    | NotBlank             | User role (ADMIN, INSTRUCTOR, EXAMINER, STUDENT) |
| `active`       | boolean | Default true         | Account active status                            |

**Relationships:**

- `userFiles` - OneToMany with UserFile
- `enrollments` - OneToMany with Enrollment (as student)
- `instructedCourses` - ManyToMany with Batch
- `conductedExams` - ManyToMany with Exam

### UserFile

Stores metadata for user files (profile pictures, etc.) stored in S3/MinIO.

**Table:** `USER_FILES`

| Field              | Type                  | Description                              |
|--------------------|-----------------------|------------------------------------------|
| `s3ObjectKey`      | String                | S3/MinIO object key                      |
| `fileUrl`          | CDN or pre-signed URL |
| `originalFilename` | String                | Original uploaded filename               |
| `contentType`      | String                | MIME type (e.g., image/jpeg)             |
| `fileSizeBytes`    | Long                  | File size in bytes                       |
| `fileType`         | FileType              | PROFILE_PICTURE, etc.                    |
| `active`           | boolean               | Only one active profile picture per user |
| `user`             | User                  | FK to owner user                         |

---

## Course Package

### Course

Represents an academic/training course.

**Table:** `COURSES`

| Field          | Type           | Description                               |
|----------------|----------------|-------------------------------------------|
| `title`        | String         | Course title                              |
| `titleLocal`   | String         | Localized title                           |
| `code`         | String         | Unique course code (e.g., JAVA-SPRING-01) |
| `description`  | String         | Course description                        |
| `thumbnailUrl` | String         | Course thumbnail image URL                |
| `courseFee`    | BigDecimal     | Course fee amount                         |
| `active`       | boolean        | Active status                             |
| `categories`   | List<Category> | Many-to-many with categories              |
| `batches`      | List<Batch>    | One-to-many with batches                  |

### Batch

Represents a cohort of students enrolled under a course.

**Table:** `BATCHES`

| Field           | Type             | Description                         |
|-----------------|------------------|-------------------------------------|
| `name`          | String           | Batch name                          |
| `nameLocal`     | String           | Localized batch name                |
| `csvFilename`   | String           | Original CSV filename               |
| `s3ObjectKey`   | String           | S3 key for CSV file                 |
| `totalRows`     | Integer          | Total rows in CSV                   |
| `enrolledCount` | Integer          | Successfully enrolled count         |
| `failedCount`   | Integer          | Failed enrollment count             |
| `skippedCount`  | Integer          | Skipped rows count                  |
| `discount`      | BigDecimal       | Discount percentage (0-100)         |
| `status`        | BatchStatus      | Processing status                   |
| `errorLog`      | String           | JSON array of error messages        |
| `course`        | Course           | FK to parent course                 |
| `instructedBy`  | List<User>       | Instructors (Many-to-many)          |
| `exams`         | List<Exam>       | Exams for this batch (Many-to-many) |
| `enrollments`   | List<Enrollment> | Student enrollments                 |

### Category

Categories for organizing courses and questions.

**Table:** `CATEGORIES`

| Field          | Type           | Description                |
|----------------|----------------|----------------------------|
| `name`         | String         | Unique category name       |
| `nameLocal`    | String         | Localized name             |
| `description`  | String         | Category description       |
| `displayOrder` | Integer        | Display order              |
| `active`       | boolean        | Active status              |
| `questions`    | List<Question> | Questions in this category |
| `courses`      | List<Course>   | Courses in this category   |

---

## Exam Package

### Exam

Represents a schedulable exam with a question bank.

**Table:** `EXAMS`

| Field                    | Type              | Description                               |
|--------------------------|-------------------|-------------------------------------------|
| `title`                  | String            | Exam title                                |
| `titleLocal`             | String            | Localized title                           |
| `description`            | String            | Exam description                          |
| `examType`               | ExamType          | MCQ, WRITTEN, MIXED, PROCTORED, TAKE_HOME |
| `examStartTime`          | LocalTime         | Daily window start time                   |
| `examEndTime`            | LocalTime         | Daily window end time                     |
| `durationMinutes`        | Integer           | Max sitting duration (10-600 min)         |
| `passMarkPercent`        | Integer           | Pass mark percentage (0-100)              |
| `totalMarks`             | Integer           | Total marks                               |
| `maxAttempts`            | Integer           | Max allowed attempts                      |
| `published`              | boolean           | Published status                          |
| `randomizeQuestions`     | boolean           | Randomize question order                  |
| `showResultImmediately`  | boolean           | Show result after submission              |
| `enrollmentFee`          | BigDecimal        | Exam enrollment fee                       |
| `certificateTemplateUrl` | String            | Certificate template URL                  |
| `conductedBy`            | List<User>        | Exam conductors (instructors)             |
| `questions`              | List<Question>    | Questions in exam                         |
| `enrollments`            | List<Enrollment>  | Student enrollments                       |
| `examAttempts`           | List<ExamAttempt> | All exam attempts                         |

### Question

A single question belonging to an exam.

**Table:** `QUESTIONS`

| Field            | Type              | Description                                                           |
|------------------|-------------------|-----------------------------------------------------------------------|
| `questionText`   | String            | Question content                                                      |
| `questionType`   | QuestionType      | SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER, LONG_ANSWER |
| `marks`          | Integer           | Marks for this question                                               |
| `orderNo`        | Integer           | Display order in exam                                                 |
| `explanation`    | String            | Answer explanation                                                    |
| `answerKeywords` | String            | Keywords for auto-grading SHORT_ANSWER                                |
| `imageUrl`       | String            | Question image URL                                                    |
| `mandatory`      | boolean           | Mandatory question flag                                               |
| `questionYear`   | Integer           | Question year (for analytics)                                         |
| `category`       | Category          | FK to category                                                        |
| `subject`        | Subject           | FK to subject                                                         |
| `exam`           | Exam              | FK to parent exam                                                     |
| `options`        | List<Option>      | Answer options                                                        |
| `answers`        | List<Answer>      | Student answers                                                       |
| `tags`           | List<QuestionTag> | Question tags                                                         |

### Option

Answer option for MCQ/True-False questions.

**Table:** `OPTIONS`

| Field          | Type     | Description             |
|----------------|----------|-------------------------|
| `optionText`   | String   | Option content          |
| `correct`      | boolean  | Correct answer flag     |
| `imageUrl`     | String   | Option image (optional) |
| `displayOrder` | Integer  | Display order           |
| `question`     | Question | FK to parent question   |

### Subject

Subject/topic for organizing questions.

**Table:** `SUBJECTS`

| Field          | Type           | Description               |
|----------------|----------------|---------------------------|
| `name`         | String         | Subject name              |
| `description`  | String         | Subject description       |
| `displayOrder` | Integer        | Display order             |
| `active`       | boolean        | Active status             |
| `questions`    | List<Question> | Questions in this subject |

### QuestionTag

Tags for labeling and filtering questions.

**Table:** `QUESTION_TAGS`

| Field       | Type           | Description                    |
|-------------|----------------|--------------------------------|
| `name`      | String         | Tag name (unique)              |
| `nameLocal` | String         | Localized name                 |
| `color`     | String         | Hex color code (e.g., #FF5733) |
| `questions` | List<Question> | Tagged questions               |

### Answer

Records a student's response to a question within an exam attempt.

**Table:** `ANSWERS`

| Field              | Type                 | Description                         |
|--------------------|----------------------|-------------------------------------|
| `textAnswer`       | String               | Free-text answer for SHORT_ANSWER   |
| `marksAwarded`     | Integer              | Marks awarded                       |
| `correct`          | Boolean              | Correct/Incorrect/Null (not graded) |
| `examinerFeedback` | String               | Examiner feedback                   |
| `flagged`          | boolean              | Flagged for review                  |
| `attempt`          | ExamAttempt          | FK to exam attempt                  |
| `question`         | Question             | FK to question                      |
| `selectedOption`   | Option               | Selected MCQ option                 |
| `submissionFiles`  | List<SubmissionFile> | Uploaded files for written answers  |

---

## Enrollment Package

### Enrollment

Junction between a student and an exam.

**Table:** `ENROLLMENTS`

| Field          | Type              | Description                                            |
|----------------|-------------------|--------------------------------------------------------|
| `status`       | EnrollmentStatus  | PENDING_PAYMENT, ACTIVE, COMPLETED, CANCELLED, EXPIRED |
| `attemptsUsed` | Integer           | Number of attempts used                                |
| `enrolledAt`   | LocalDateTime     | Enrollment timestamp                                   |
| `expiresAt`    | LocalDateTime     | Enrollment expiration                                  |
| `feeWaived`    | boolean           | Fee waiver flag                                        |
| `student`      | User              | FK to student                                          |
| `exam`         | Exam              | FK to exam                                             |
| `batch`        | Batch             | FK to batch (optional)                                 |
| `attempts`     | List<ExamAttempt> | Exam attempts                                          |
| `payment`      | Payment           | Associated payment                                     |

### ExamAttempt

Represents one student sitting of an exam.

**Table:** `EXAM_ATTEMPTS`

| Field             | Type                 | Description                 |
|-------------------|----------------------|-----------------------------|
| `attemptNumber`   | Integer              | Attempt number (1, 2, 3...) |
| `startedAt`       | LocalDateTime        | When attempt started        |
| `submittedAt`     | LocalDateTime        | When attempt submitted      |
| `score`           | Integer              | Raw score                   |
| `scorePercent`    | Double               | Score percentage            |
| `passed`          | Boolean              | Pass/fail status            |
| `submitted`       | boolean              | Submission status           |
| `graded`          | boolean              | Grading status              |
| `ipAddress`       | String               | Student IP address          |
| `certificateUrl`  | String               | Generated certificate URL   |
| `version`         | Long                 | Optimistic locking version  |
| `enrollment`      | Enrollment           | FK to enrollment            |
| `exam`            | Exam                 | FK to exam                  |
| `answers`         | List<Answer>         | Student answers             |
| `submissionFiles` | List<SubmissionFile> | All uploaded files          |

### SubmissionFile

File uploaded by student for written-exam answers.

**Table:** `SUBMISSION_FILES`

| Field              | Type        | Description                        |
|--------------------|-------------|------------------------------------|
| `s3ObjectKey`      | String      | S3/MinIO object key                |
| `fileUrl`          | String      | Download URL                       |
| `originalFilename` | String      | Original filename                  |
| `contentType`      | String      | MIME type                          |
| `fileSizeBytes`    | Long        | File size                          |
| `fileType`         | FileType    | SUBMISSION_PDF or SUBMISSION_IMAGE |
| `pageNumber`       | Integer     | Page sequence number               |
| `reviewed`         | boolean     | Examiner reviewed flag             |
| `thumbnailUrl`     | String      | Thumbnail URL for images           |
| `answer`           | Answer      | FK to answer                       |
| `attempt`          | ExamAttempt | FK to attempt (redundant FK)       |

---

## Payment Package

### Payment

Aggregate payment record for one enrollment.

**Table:** `PAYMENTS`

| Field          | Type                     | Description                                       |
|----------------|--------------------------|---------------------------------------------------|
| `amountDue`    | BigDecimal               | Total amount due                                  |
| `amountPaid`   | BigDecimal               | Total amount paid                                 |
| `currency`     | String                   | Currency code (default USD)                       |
| `status`       | PaymentStatus            | UNPAID, PARTIALLY_PAID, PAID, REFUNDED, CANCELLED |
| `notes`        | String                   | Additional notes                                  |
| `enrollment`   | Enrollment               | FK to enrollment                                  |
| `transactions` | List<PaymentTransaction> | Payment transactions                              |

### PaymentTransaction

Individual charge attempt, retry, or refund event.

**Table:** `PAYMENT_TRANSACTIONS`

| Field                   | Type              | Description                                                 |
|-------------------------|-------------------|-------------------------------------------------------------|
| `stripeChargeId`        | String            | Stripe charge ID (ch_xxx)                                   |
| `stripePaymentIntentId` | String            | Stripe PaymentIntent ID (pi_xxx)                            |
| `stripeRefundId`        | String            | Stripe refund ID (re_xxx)                                   |
| `amount`                | BigDecimal        | Transaction amount                                          |
| `currency`              | String            | Currency code                                               |
| `status`                | TransactionStatus | PENDING, PROCESSING, SUCCEEDED, FAILED, REFUNDED, CANCELLED |
| `paymentMethod`         | String            | Payment method (card, bank_transfer, etc.)                  |
| `paymentMethodDetail`   | String            | Display text (e.g., "Visa ···4242")                         |
| `transactedAt`          | LocalDateTime     | Transaction timestamp                                       |
| `failureReason`         | String            | Failure reason from payment gateway                         |
| `webhookPayload`        | String            | Raw webhook JSON                                            |
| `payment`               | Payment           | FK to parent payment                                        |

---

## Enums

### Role

System-wide user roles.

| Value             | Description          |
|-------------------|----------------------|
| `ROLE_ADMIN`      | System administrator |
| `ROLE_INSTRUCTOR` | Course instructor    |
| `ROLE_EXAMINER`   | Exam grader          |
| `ROLE_STUDENT`    | Student user         |

### ExamType

Exam delivery formats.

| Value       | Description                                |
|-------------|--------------------------------------------|
| `MCQ`       | Auto-graded multiple-choice only           |
| `WRITTEN`   | Student uploads PDF/images; manual grading |
| `MIXED`     | MCQ + written-upload sections              |
| `PROCTORED` | Live webcam-supervised sitting             |
| `TAKE_HOME` | Time-windowed, no live supervision         |

### QuestionType

Question types within an exam.

| Value             | Description                                 |
|-------------------|---------------------------------------------|
| `SINGLE_CHOICE`   | One correct answer                          |
| `MULTIPLE_CHOICE` | Multiple correct answers                    |
| `TRUE_FALSE`      | True/False question                         |
| `SHORT_ANSWER`    | Short text answer (auto-graded by keywords) |
| `LONG_ANSWER`     | Long answer (manual grading)                |

### EnrollmentStatus

Lifecycle states of student enrollment.

| Value             | Description                   |
|-------------------|-------------------------------|
| `PENDING_PAYMENT` | Awaiting payment              |
| `ACTIVE`          | Enrolled and can attempt exam |
| `COMPLETED`       | Successfully completed        |
| `CANCELLED`       | Cancelled by admin or student |
| `EXPIRED`         | Enrollment period expired     |

### PaymentStatus

Aggregate payment status.

| Value            | Description              |
|------------------|--------------------------|
| `UNPAID`         | No payment received      |
| `PARTIALLY_PAID` | Partial payment received |
| `PAID`           | Fully paid               |
| `REFUNDED`       | Payment refunded         |
| `CANCELLED`      | Payment cancelled        |

### TransactionStatus

Individual transaction status (mirrors Stripe).

| Value        | Description            |
|--------------|------------------------|
| `PENDING`    | Awaiting processing    |
| `PROCESSING` | Currently processing   |
| `SUCCEEDED`  | Successfully completed |
| `FAILED`     | Failed                 |
| `REFUNDED`   | Refunded               |
| `CANCELLED`  | Cancelled              |

### BatchStatus

CSV batch processing states.

| Value              | Description                     |
|--------------------|---------------------------------|
| `UPLOADED`         | CSV uploaded, not yet processed |
| `PROCESSING`       | Currently processing            |
| `COMPLETED`        | Successfully processed          |
| `PARTIALLY_FAILED` | Some rows failed                |
| `FAILED`           | All processing failed           |

### FileType

Classifies stored files.

| Value              | Description           |
|--------------------|-----------------------|
| `PROFILE_PICTURE`  | User profile picture  |
| `SUBMISSION_PDF`   | PDF submission        |
| `SUBMISSION_IMAGE` | Image submission      |
| `CERTIFICATE`      | Generated certificate |
| `EXAM_ATTACHMENT`  | Exam attachment       |

---

## Entity Relationships Diagram

```
User
├── UserFile (1:N)
├── Enrollment (1:N) ─────┐
│                         │
Exam                      │
├── Question (1:N)        │
│   ├── Option (1:N)      │
│   ├── Answer (1:N) ─────┼──→ ExamAttempt (1:N) ──→ Answer (1:N) ──→ SubmissionFile (1:N)
│   └── QuestionTag (N:M)│    │
├── ExamAttempt (1:N)    │    └── SubmissionFile (1:N)
├── Batch (N:M)          │
├── ConductedBy (N:M)    │
│                       Enrollment (N:1)
Batch ───────────────────┘
├── Course (N:1)
├── User (N:M) - instructors
├── Exam (N:M)
└── Enrollment (1:N)

Course
├── Category (N:M)
└── Batch (1:N)

Payment (1:1) ← Enrollment
└── PaymentTransaction (1:N)
```

---

## Database Indexes

Key indexes in the system:

| Table                | Index                           | Columns          |
|----------------------|---------------------------------|------------------|
| USERS                | IDX_USERS_EMAIL                 | EMAIL            |
| USERS                | IDX_USERS_ROLE                  | ROLE             |
| USER_FILES           | IDX_USER_FILES_USER_ID          | USER_ID          |
| USER_FILES           | IDX_USER_FILES_FILE_TYPE        | FILE_TYPE        |
| COURSES              | IDX_COURSES_CODE                | CODE             |
| BATCHES              | IDX_BATCHES_COURSE_ID           | COURSE_ID        |
| BATCHES              | IDX_BATCHES_STATUS              | STATUS           |
| CATEGORIES           | IDX_CATEGORIES_NAME             | NAME             |
| EXAMS                | IDX_EXAMS_EXAM_TYPE             | EXAM_TYPE        |
| EXAMS                | IDX_EXAMS_START_TIME            | EXAM_START_TIME  |
| QUESTIONS            | IDX_QUESTIONS_EXAM_ID           | EXAM_ID          |
| QUESTIONS            | IDX_QUESTIONS_ORDER_NO          | ORDER_NO         |
| OPTIONS              | IDX_OPTIONS_QUESTION_ID         | QUESTION_ID      |
| SUBJECTS             | IDX_SUBJECTS_NAME               | NAME             |
| QUESTION_TAGS        | IDX_QUESTION_TAGS_NAME          | NAME             |
| ANSWERS              | IDX_ANSWERS_ATTEMPT_ID          | ATTEMPT_ID       |
| ANSWERS              | IDX_ANSWERS_QUESTION_ID         | QUESTION_ID      |
| ENROLLMENTS          | IDX_ENROLLMENTS_STUDENT_ID      | STUDENT_ID       |
| ENROLLMENTS          | IDX_ENROLLMENTS_EXAM_ID         | EXAM_ID          |
| ENROLLMENTS          | IDX_ENROLLMENTS_BATCH_ID        | BATCH_ID         |
| ENROLLMENTS          | IDX_ENROLLMENTS_STATUS          | STATUS           |
| EXAM_ATTEMPTS        | IDX_EXAM_ATTEMPTS_ENROLLMENT_ID | ENROLLMENT_ID    |
| EXAM_ATTEMPTS        | IDX_EXAM_ATTEMPTS_STARTED_AT    | STARTED_AT       |
| SUBMISSION_FILES     | IDX_SUB_FILES_ANSWER_ID         | ANSWER_ID        |
| SUBMISSION_FILES     | IDX_SUB_FILES_ATTEMPT_ID        | ATTEMPT_ID       |
| PAYMENTS             | IDX_PAYMENTS_ENROLLMENT_ID      | ENROLLMENT_ID    |
| PAYMENTS             | IDX_PAYMENTS_STATUS             | STATUS           |
| PAYMENT_TRANSACTIONS | IDX_PAY_TXN_PAYMENT_ID          | PAYMENT_ID       |
| PAYMENT_TRANSACTIONS | IDX_PAY_TXN_STATUS              | STATUS           |
| PAYMENT_TRANSACTIONS | IDX_PAY_TXN_STRIPE_CHARGE_ID    | STRIPE_CHARGE_ID |
| PAYMENT_TRANSACTIONS | IDX_PAY_TXN_TRANSACTED_AT       | TRANSACTED_AT    |
