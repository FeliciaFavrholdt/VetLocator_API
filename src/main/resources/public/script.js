// Function to handle vet search
function searchVet() {
    // Get the value of the search input field
    const searchInput = document.getElementById('searchInput').value;

    // Simulate a search result (In real scenarios, you'd use this value to query a server or filter data)
    if (searchInput.trim() === "") {
        alert("Please enter a location or veterinarian name to search.");
    } else {
        console.log("Searching for veterinarian near:", searchInput);
        alert(`Searching for veterinarians near "${searchInput}"`);
        // Add further logic to handle actual vet search (e.g., API request)
    }
}
