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
  "fullName": "John Doe",
  "email": "johndoe@example.com",
  "password": "StrongPassword123"
}
```

#### Response:
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
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
  "userId": "123e4567-e89b-12d3-a456-426614174000",
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
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "johndoe@example.com",
    "password": "hashed-password",
    "createdAt": "2025-08-29T10:15:30Z",
    "updatedAt": "2025-08-29T10:15:30Z"
  },
  {
    "userId": "987e6543-e21b-65d3-a456-426614174999",
    "fullName": "Jane Smith",
    "email": "janesmith@example.com",
    "password": "hashed-password",
    "createdAt": "2025-08-29T10:20:45Z",
    "updatedAt": "2025-08-29T10:20:45Z"
  }
]
```
