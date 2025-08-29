# Authentication & User Management API
## Authentication
### 1. Sign Up

#### Endpoint:
```bash
POST /auth/signup
```

#### Description:
Register a new user account.

#### Consumers:
`Anyone`

#### Request Body Example:
```json
{
  "userId": "John123",
  "email": "johndoe@example.com",
  "password": "StrongPassword123"
}
```

#### Response:
```json
{
  "userId": "John123",
  "fullName": "John Doe",
  "email": "johndoe@example.com",
  "password": "hashed-password",
  "createdAt": "2025-08-29T10:15:30Z",
  "updatedAt": "2025-08-29T10:15:30Z"
}
```
### 2. Login

#### Endpoint:
```bash
POST /auth/login
```

#### Description:
Authenticate user credentials and issue a JWT access token.

#### Consumers:
`Anyone`

#### Request Body Example:
```json
{
  "email": "johndoe@example.com",
  "password": "StrongPassword123"
}
```

#### Response:
```json
{
  "token": "jwt-token-string",
  "expiresIn": 3600
}
```

## User Management
### 3. Get Current User

#### Endpoint:
```bash
GET /users/me
```
#### Description:
Retrieve the authenticated user’s profile information.

#### Consumers:
`Authorized User`

#### Authorization:
Requires a valid JWT in the Authorization header:
```
Authorization: Bearer <token>
```

#### Response:
```json
{
  "userId": "John123",
  "fullName": "John Doe",
  "email": "johndoe@example.com",
  "password": "hashed-password",
  "createdAt": "2025-08-29T10:15:30Z",
  "updatedAt": "2025-08-29T10:15:30Z"
}
```
### 4. Get All Users

#### Endpoint:
```bash
GET /users/
```

#### Description:
Retrieve a list of all registered users.

#### Consumers:
`Authorized User`

#### Authorization:
Requires a valid JWT in the Authorization header:
```
Authorization: Bearer <token>
```
#### Response:
```json
[
  {
    "userId": "John123",
    "fullName": "John Doe",
    "email": "johndoe@example.com",
    "password": "hashed-password",
    "createdAt": "2025-08-29T10:15:30Z",
    "updatedAt": "2025-08-29T10:15:30Z"
  },
  {
    "userId": "Jane456",
    "fullName": "Jane Smith",
    "email": "janesmith@example.com",
    "password": "hashed-password",
    "createdAt": "2025-08-29T10:20:45Z",
    "updatedAt": "2025-08-29T10:20:45Z"
  }
]
```

