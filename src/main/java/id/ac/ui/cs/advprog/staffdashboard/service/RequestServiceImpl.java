    package id.ac.ui.cs.advprog.staffdashboard.service;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.ResponseEntity;
    import org.springframework.scheduling.annotation.Async;
    import org.springframework.web.client.RestTemplate;

    import java.util.Collection;
    import java.util.concurrent.CompletableFuture;

    public abstract class RequestServiceImpl<T> implements StaffDashboardService<T>{


        protected JpaRepository<T, String> requestRepository;

        //Change url depending  authentication source url
        public static String authUrl;

        @Value("${auth.url}")
        public void setAuthUrl(String url){
            authUrl=url;
        }

        @Override
        public T add(T request){
            return requestRepository.save(request);
        }

        @Override
        public Collection<T> findAll(){return requestRepository.findAll();}

        @Override
        public T findById(String id) {
            return requestRepository.findById(id).orElse(null);
        }


        @Override
        public CompletableFuture<Void> deleteById(String id){
            requestRepository.deleteById(id);
            return null;
        }

        @Override
        @Async
        public CompletableFuture<Void> deleteAll() {
            requestRepository.deleteAll();
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public Boolean authenticateStaff(String token) throws Exception {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", token);
                HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
                ResponseEntity<String> response = restTemplate.exchange(authUrl+"/user/get-role", HttpMethod.GET, httpEntity, String.class);
                String userRole = response.getBody();

                if (!userRole.equals("STAFF")) {
                    throw new Exception("Non STAFF aren't Allowed");
                }

                return true;
            } catch(Exception e) {
                System.out.println(e.getMessage());
                return false;}

        }
        public T updateStatusProcess(T request, String verdict, String token) throws Exception {
            try{
                deleteAll();
                collectRequest(token);
                allowToReview(request,verdict,token);
                createOrder(request,verdict, token);
                return  updateStatus(request,verdict,token);

            }catch(Exception e){
                System.err.println("An error occurred during updateStatusProcess: " + e.getMessage());
                throw e;
            }
        }

        public abstract T updateStatus(T request, String verdict, String token);
        public abstract Collection<T> collectRequest(String token) throws Exception;
        public void allowToReview(T request,String verdict,String token){};
        public void createOrder(T request,String verdict, String token){};

    }
