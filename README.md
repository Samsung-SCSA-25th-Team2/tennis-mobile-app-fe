# 테니스장 앱 아키텍처 학습 가이드

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [전체 아키텍처](#전체-아키텍처)
3. [계층별 상세 설명](#계층별-상세-설명)
4. [데이터 흐름](#데이터-흐름)
5. [주요 개념 설명](#주요-개념-설명)
6. [실제 동작 흐름](#실제-동작-흐름)

---

## 프로젝트 개요

이 프로젝트는 **테니스장 목록 조회**와 **상세 정보 조회** 기능을 제공하는 Android 앱입니다.

### 사용한 주요 기술
- **Kotlin**: Android 공식 프로그래밍 언어
- **Jetpack Compose**: 최신 UI 프레임워크
- **MVVM 패턴**: UI와 비즈니스 로직 분리
- **Clean Architecture**: 계층 분리로 유지보수성 향상
- **Retrofit**: REST API 통신 라이브러리
- **Coroutine**: 비동기 처리

---

## 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                         UI Layer                            │
│  (사용자가 보는 화면 - Jetpack Compose)                        │
│                                                             │
│  CourtListScreen  ←→  CourtListViewModel                   │
│  CourtDetailScreen ←→ CourtDetailViewModel                 │
│                                                             │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ↓ Repository Interface를 통해 통신
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
│  (비즈니스 로직과 규칙 - 앱의 핵심)                             │
│                                                             │
│  TennisCourtRepository (Interface)                          │
│  TennisCourt (Model)                                        │
│                                                             │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ↓ Repository 구현체가 실제 데이터 가져오기
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                            │
│  (실제 데이터 가져오기 - API, DB 등)                           │
│                                                             │
│  TennisCourtRepositoryImpl                                  │
│  TennisCourtApiService (Retrofit)                           │
│  TennisCourtDto (서버에서 받는 데이터 형식)                    │
│                                                             │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ↓ HTTP 요청
            ┌────────────┐
            │   서버      │
            │  (REST API) │
            └────────────┘
```

---

## 계층별 상세 설명

### 1. UI Layer (화면 계층)

사용자와 직접 상호작용하는 부분입니다.

#### 구성 요소

**1) Screen (화면)**
```kotlin
// 실제 화면을 그리는 Composable 함수
CourtListScreen(
    onCourtClick = { courtId -> /* 테니스장 클릭시 동작 */ }
)
```
- UI 요소만 담당 (버튼, 리스트, 이미지 등)
- 비즈니스 로직 없음
- ViewModel에서 데이터를 받아서 표시만 함

**2) ViewModel (화면 상태 관리)**
```kotlin
class CourtListViewModel : ViewModel() {
    // 화면 상태를 담는 StateFlow
    private val _uiState = MutableStateFlow(CourtListUiState())
    val uiState: StateFlow<CourtListUiState> = _uiState.asStateFlow()

    // 데이터 로드 함수
    fun loadCourts() {
        viewModelScope.launch {
            // Repository에서 데이터 가져오기
            val courts = repository.getAllCourts()
            _uiState.value = CourtListUiState(courts = courts)
        }
    }
}
```

**역할:**
- 화면에 표시할 데이터를 관리
- Repository에서 데이터를 가져옴
- 화면이 회전되어도 데이터 유지 (생명주기 독립적)

**3) UiState (화면 상태 데이터)**
```kotlin
data class CourtListUiState(
    val isLoading: Boolean = false,
    val courtList: List<TennisCourt> = emptyList(),
    val errorMessage: String? = null
)
```
- 화면에 표시될 모든 정보를 담음
- 로딩 중인지, 에러가 있는지 등 화면 상태 관리

---

### 2. Domain Layer (도메인 계층)

앱의 핵심 비즈니스 로직을 담당합니다. **플랫폼 독립적**이어야 합니다.

#### 구성 요소

**1) Repository Interface (저장소 인터페이스)**
```kotlin
interface TennisCourtRepository {
    suspend fun getCourt(courtId: Long): TennisCourt
    suspend fun getAllCourts(): List<TennisCourt>
}
```

**왜 Interface를 사용할까?**
- **의존성 역전**: ViewModel은 구체적인 구현체가 아닌 인터페이스에만 의존
- **테스트 용이**: 가짜(Mock) 데이터로 쉽게 테스트 가능
- **유연성**: API를 DB로 바꿔도 ViewModel 코드는 변경 불필요

**2) Model (도메인 모델)**
```kotlin
data class TennisCourt(
    val courtId: Long,
    val name: String,
    val address: String,
    val thumbnail: String,
    val latitude: Double,
    val longitude: Double
)
```
- 앱 전체에서 사용하는 **핵심 데이터 구조**
- 서버 응답 형식(DTO)과는 별개로 존재

---

### 3. Data Layer (데이터 계층)

실제 데이터를 가져오고 저장하는 역할을 합니다.

#### 구성 요소

**1) Repository 구현체**
```kotlin
class TennisCourtRepositoryImpl(
    private val apiService: TennisCourtApiService
) : TennisCourtRepository {

    override suspend fun getCourt(courtId: Long): TennisCourt {
        // API에서 DTO 받아오기
        val dto = apiService.getCourt(courtId)
        // DTO를 Domain Model로 변환
        return dto.toDomain()
    }

    override suspend fun getAllCourts(): List<TennisCourt> {
        val response = apiService.searchCourts()
        return response.courts.map { it.toDomain() }
    }
}
```

**2) API Service (Retrofit)**
```kotlin
interface TennisCourtApiService {
    @GET("api/v1/tennis-courts/{id}")
    suspend fun getCourt(@Path("id") courtId: Long): TennisCourtDto

    @GET("api/v1/tennis-courts/search")
    suspend fun searchCourts(
        @Query("keyword") keyword: String = "서울",
        @Query("size") size: Int = 100
    ): TennisCourtSearchResponseDto
}
```

**3) DTO (Data Transfer Object)**
```kotlin
// 서버에서 받는 JSON 데이터 형식
data class TennisCourtDto(
    val courtId: Long,
    val name: String,
    val address: String,
    // ... 서버 응답 형식 그대로
)

// Domain Model로 변환하는 확장 함수
fun TennisCourtDto.toDomain(): TennisCourt {
    return TennisCourt(
        courtId = this.courtId,
        name = this.name,
        address = this.address,
        // ...
    )
}
```

**DTO를 따로 만드는 이유?**
- 서버 응답 형식이 바뀌어도 Domain Model은 영향 없음
- 서버 데이터에 불필요한 필드가 있어도 필요한 것만 사용
- 데이터 변환 로직을 한 곳에서 관리

---

## 데이터 흐름

### 목록 조회 흐름

```
1. 사용자가 앱 실행
   ↓
2. CourtListScreen이 화면에 표시됨
   ↓
3. CourtListViewModel.loadCourts() 호출
   ↓
4. Repository.getAllCourts() 호출
   ↓
5. TennisCourtApiService.searchCourts() 실행
   ↓
6. 서버에 HTTP GET 요청
   ↓
7. 서버가 JSON 응답 (TennisCourtSearchResponseDto)
   ↓
8. DTO를 Domain Model(TennisCourt)로 변환
   ↓
9. ViewModel이 UiState 업데이트
   ↓
10. Screen이 자동으로 리컴포지션되어 목록 표시
```

### 상세 조회 흐름

```
1. 사용자가 목록에서 테니스장 클릭
   ↓
2. onCourtClick(courtId) 콜백 실행
   ↓
3. Navigation: "courtDetail/123" 경로로 이동
   ↓
4. CourtDetailRoute에서 courtId 파라미터 추출
   ↓
5. CourtDetailViewModel.loadCourt(courtId) 호출
   ↓
6. Repository.getCourt(courtId) 호출
   ↓
7. API 요청: GET /api/v1/tennis-courts/123
   ↓
8. 서버 응답을 Domain Model로 변환
   ↓
9. ViewModel이 UiState 업데이트
   ↓
10. CourtDetailScreen에 상세 정보 표시
```

---

## 주요 개념 설명

### 1. MVVM 패턴

**Model - View - ViewModel** 구조로 UI와 로직을 분리합니다.

```
View (Screen)          ViewModel              Model (Repository)
    │                      │                          │
    │   observe uiState    │                          │
    │◄─────────────────────│                          │
    │                      │                          │
    │   user action        │                          │
    │─────────────────────►│                          │
    │                      │   데이터 요청              │
    │                      │─────────────────────────►│
    │                      │                          │
    │                      │◄─────────────────────────│
    │                      │   데이터 응답              │
    │                      │                          │
    │   uiState 변경됨      │                          │
    │◄─────────────────────│                          │
```

**장점:**
- UI 코드와 비즈니스 로직 분리
- 테스트하기 쉬움
- 화면 회전 등의 상황에서 데이터 유지

### 2. StateFlow와 상태 관리

```kotlin
// ViewModel 내부
private val _uiState = MutableStateFlow(CourtListUiState())
val uiState: StateFlow<CourtListUiState> = _uiState.asStateFlow()

// Screen에서 구독
val uiState by viewModel.uiState.collectAsState()
```

**StateFlow란?**
- 상태를 담는 Flow (Kotlin의 비동기 스트림)
- 값이 변경되면 자동으로 UI 업데이트
- Compose와 완벽한 호환

**왜 두 개로 분리?**
- `_uiState` (MutableStateFlow): ViewModel 내부에서만 수정 가능
- `uiState` (StateFlow): 외부에서는 읽기만 가능
- 캡슐화를 통해 의도치 않은 상태 변경 방지

### 3. Coroutine과 suspend 함수

```kotlin
suspend fun getAllCourts(): List<TennisCourt> {
    // 네트워크 호출 - 메인 스레드를 블로킹하지 않음
    return apiService.searchCourts().courts.map { it.toDomain() }
}
```

**suspend 키워드:**
- 일시 중단 가능한 함수
- 네트워크 요청 등 오래 걸리는 작업을 비동기로 처리
- 메인 스레드를 블로킹하지 않아 UI가 멈추지 않음

**viewModelScope:**
```kotlin
viewModelScope.launch {
    val courts = repository.getAllCourts()
}
```
- ViewModel의 생명주기에 맞춰 코루틴 실행
- ViewModel이 종료되면 자동으로 코루틴도 취소

### 4. Jetpack Navigation

**Route 정의:**
```kotlin
sealed class NavigationRoute(val route: String) {
    object CourtList : NavigationRoute("courtList")

    object CourtDetail : NavigationRoute("courtDetail/{courtId}") {
        fun createRoute(courtId: Long): String = "courtDetail/$courtId"
    }
}
```

**Navigation 설정:**
```kotlin
composable(
    route = NavigationRoute.CourtDetail.route,
    arguments = listOf(
        navArgument("courtId") {
            type = NavType.LongType  // URL 파라미터를 Long으로 파싱
        }
    )
) { backStackEntry ->
    val courtId = backStackEntry.arguments?.getLong("courtId") ?: 1L
    CourtDetailRoute(courtId = courtId)
}
```

**동작 방식:**
1. 목록에서 클릭: `navController.navigate("courtDetail/123")`
2. Navigation이 URL 파싱: `courtId = 123`
3. CourtDetailRoute에 전달: `CourtDetailRoute(courtId = 123)`

---

## 실제 동작 흐름

### 시나리오: 사용자가 앱을 열고 테니스장을 클릭하여 상세 화면을 봄

#### Step 1: 앱 시작 및 목록 표시

```kotlin
// MainActivity.kt
setContent {
    TennisMobileAppTheme {
        AppNavGraph(navController = rememberNavController())
    }
}

// AppNavGraph.kt - 목록 화면
composable(NavigationRoute.CourtList.route) {
    CourtListScreen(
        onCourtClick = { courtId ->
            navController.navigate(NavigationRoute.CourtDetail.createRoute(courtId))
        }
    )
}

// CourtListScreen.kt
@Composable
fun CourtListScreen(
    viewModel: CourtListViewModel = viewModel()
) {
    // ViewModel 초기화 시 자동으로 데이터 로드
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> CircularProgressIndicator()
        uiState.courtList.isNotEmpty() -> {
            LazyColumn {
                items(uiState.courtList) { court ->
                    CourtItemCard(court = court, onClick = onCourtClick)
                }
            }
        }
    }
}

// CourtListViewModel.kt
class CourtListViewModel : ViewModel() {
    init {
        loadCourts()  // ViewModel 생성 시 자동 로드
    }

    fun loadCourts() {
        viewModelScope.launch {
            _uiState.value = CourtListUiState(isLoading = true)

            try {
                val courts = repository.getAllCourts()
                _uiState.value = CourtListUiState(
                    isLoading = false,
                    courtList = courts
                )
            } catch (e: Exception) {
                _uiState.value = CourtListUiState(
                    isLoading = false,
                    errorMessage = "데이터를 불러올 수 없습니다"
                )
            }
        }
    }
}
```

#### Step 2: 테니스장 클릭

```kotlin
// 사용자가 courtId=123인 테니스장 클릭
CourtItemCard(
    court = court,
    onClick = { courtId ->
        onCourtClick(courtId)  // courtId = 123
    }
)

// onCourtClick 실행
onCourtClick = { courtId ->
    // "courtDetail/123"으로 네비게이션
    navController.navigate(NavigationRoute.CourtDetail.createRoute(courtId))
}
```

#### Step 3: 상세 화면 표시

```kotlin
// AppNavGraph.kt - 상세 화면 라우트
composable(
    route = "courtDetail/{courtId}",
    arguments = listOf(
        navArgument("courtId") { type = NavType.LongType }
    )
) { backStackEntry ->
    // URL에서 courtId 추출
    val courtId = backStackEntry.arguments?.getLong("courtId") ?: 1L

    CourtDetailRoute(
        courtId = courtId,  // courtId = 123
        onBackClick = { navController.popBackStack() }
    )
}

// CourtDetailRoute.kt
@Composable
fun CourtDetailRoute(
    courtId: Long,
    viewModel: CourtDetailViewModel = viewModel()
) {
    // courtId가 변경될 때마다 데이터 로드
    LaunchedEffect(courtId) {
        viewModel.loadCourt(courtId)
    }

    val uiState by viewModel.uiState.collectAsState()
    CourtDetailScreen(uiState = uiState, onBackClick = onBackClick)
}

// CourtDetailViewModel.kt
fun loadCourt(courtId: Long) {
    viewModelScope.launch {
        _uiState.value = CourtDetailUiState(isLoading = true)

        try {
            val court = repository.getCourt(courtId = courtId)
            _uiState.value = CourtDetailUiState(
                isLoading = false,
                courtName = court.name,
                address = court.address,
                thumbnail = court.thumbnail,
                latitude = court.latitude,
                longitude = court.longitude
            )
        } catch (e: Exception) {
            _uiState.value = CourtDetailUiState(
                isLoading = false,
                errorMessage = "상세 정보를 불러올 수 없습니다"
            )
        }
    }
}
```

#### Step 4: API 요청 및 응답

```kotlin
// TennisCourtRepositoryImpl.kt
override suspend fun getCourt(courtId: Long): TennisCourt {
    // Retrofit을 통한 API 호출
    val dto = apiService.getCourt(courtId)
    // DTO를 Domain Model로 변환
    return dto.toDomain()
}

// TennisCourtApiService.kt
@GET("api/v1/tennis-courts/{id}")
suspend fun getCourt(@Path("id") courtId: Long): TennisCourtDto

// 실제 HTTP 요청
// GET https://your-api.com/api/v1/tennis-courts/123

// 서버 응답 (JSON)
{
  "courtId": 123,
  "name": "서울 테니스장",
  "address": "서울시 강남구...",
  "thumbnail": "https://...",
  "latitude": 37.123,
  "longitude": 127.456
}

// Retrofit이 자동으로 TennisCourtDto로 변환
// toDomain()으로 TennisCourt로 변환
// ViewModel에 전달
// UI 업데이트
```

---

## 핵심 포인트 정리

### 1. 계층 분리의 장점
- **UI Layer**: 화면 그리기만 집중
- **Domain Layer**: 비즈니스 로직에만 집중
- **Data Layer**: 데이터 가져오기만 집중

각 계층은 **독립적**이라 하나를 바꿔도 다른 계층에 영향 없음

### 2. 의존성 방향
```
UI Layer → Domain Layer ← Data Layer
```
- UI와 Data는 Domain을 알지만, Domain은 둘을 모름
- Domain은 순수한 Kotlin 코드로만 구성 (Android 의존성 없음)

### 3. 데이터 변환
```
서버 JSON → DTO → Domain Model → UiState → UI
```
각 단계마다 적절한 형태로 변환

### 4. 비동기 처리
- `suspend` 함수로 네트워크 요청
- `StateFlow`로 상태 관리
- `viewModelScope`로 생명주기 관리

### 5. Navigation
- URL 기반 라우팅
- 타입 안전한 파라미터 전달
- 백스택 자동 관리

---

## 추가 학습 자료

### 공식 문서
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Navigation](https://developer.android.com/jetpack/compose/navigation)
- [Retrofit](https://square.github.io/retrofit/)

### 권장 학습 순서
1. Kotlin 기초 (suspend, coroutine)
2. Jetpack Compose 기초
3. MVVM 패턴 이해
4. StateFlow와 상태 관리
5. Retrofit과 네트워크 통신
6. Clean Architecture 원칙
