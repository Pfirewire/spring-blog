// Using javascript and ajax to take a json string and output html for viewing all posts
// This is very rudimentary and would have to be expanded upon with stying to be useful

$(() => {
    console.log("Testing devtools");

    let request = $.ajax({'url': '/posts.json'});
    request.done(posts => {
        let html = '';
        posts.forEach(post => {
            html += `
            <div>
                <h1>${post.title}</h1>
                <p>${post.body}</p>
                <p>Created by ${post.user.username}</p>
            </div>
            `;
        });
        $('#posts').html(html);
    });
});