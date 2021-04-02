/**
 * @Author Chase Staples
 * @DateCreated 03/31/21
 *
 *
 */

async function fetchAPI(li) {
    let url = "/" + li.srcElement.parentElement.id;
    fetch(url)
        .then(response => {
            if(response.ok){
                return response.json();
            }
            else{
                console.log("An Error Occurred")
                throw Error("Error");
            }
        })
        .then(data => {
            console.log(data);

        },
            error => {
                console.log(`Error in fetch api: ${error}`);
            })
        .catch(error =>{
            console.log(error);
        })
    postData();
}

function postData(url){
    const name = url.srcElement.innerHTML;
    const submenu = url.srcElement.parentElement.id;
    const navbar = window.sessionStorage.getItem('nav');

    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: ({
            name: name;
            submenu: submenu;
            navbar: navbar;
        })
    })
        .then(response => {
            if(response.ok){
                return response.json();
            }
            else{
                console.log("An Error Occurred")
                throw Error("Error");
            }
        },
            errorReason => {
            console.log(`An error occurred in the fetch api\n reason: ${errorReason}`);
            })
        .then(data => {
            console.log(data)
        });
}