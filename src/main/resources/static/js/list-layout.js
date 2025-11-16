function sortPosts()
{
    const urlParams = new URLSearchParams(window.location.search);
    const sortType = 'sortType';

    const sortTypeValue = document.getElementById('sort-select').value;

    // Change its value as needed
    urlParams.set('page', 1);
    urlParams.set(sortType, sortTypeValue); // update with the select box value

    // Reflect the changes in the browser's URL
    const newUrl = window.location.pathname + '?' + urlParams.toString();
    window.history.replaceState({}, '', newUrl);

    // Send a GET request to the updated URL
    fetch(newUrl, {
        method: 'GET',
        headers: {
            'Accept': 'text/html'
        }
    })
        .then(response =>
        {
            if (!response.ok)
            {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html =>
        {
            document.documentElement.innerHTML = html;
        })
        .catch(error =>
        {
            console.error('There was a problem with the fetch operation:', error);
        });
}
