import networking.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {
        CompletableFuture<String>[] futures = new CompletableFuture[workersAddresses.size()];
        for (int i = 0; i < workersAddresses.size(); i++) {
            String worker = workersAddresses.get(i);
            String task = tasks.get(i);
            byte[] payload = task.getBytes();

            futures[i] = webClient.sendTask(worker, payload);
        }

        List<String> results = Stream.of(futures).map(CompletableFuture::join)
                .collect(Collectors.toList());
        return results;
    }
}
