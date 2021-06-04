let server = new Socket()
server.on('message', handleMessage)

let myId
const voteButton = document.getElementsByTagName('button')

function handleMessage(message) {

  if (message.type === 'welcome') {
    server.send({ type: 'welcome', odlId: myId })
    myId = message.id
    document.getElementById("userid").innerText = message.id
    updateVote(message.vote)
  }

  if (message.type === 'update') {
    updateVote(message.vote)
  }
}

window.addEventListener('load', event => {
  server.init()

  for (const key in voteButton) {
    if (voteButton.hasOwnProperty(key)) {
      const button = voteButton[key]
      button.addEventListener('click', (e) => {
        server.send({ type: 'vote', vote: button.dataset.vote })
        disableButton()
      })
    }
  }
})

function disableButton() {
  for (const key in voteButton) {
    if (voteButton.hasOwnProperty(key)) {
      const button = voteButton[key]
      button.disabled = true
    }
  }
}

function updateVote (vote) {
  document.getElementById('kobami').innerHTML = vote.kobami
  document.getElementById('jokawi').innerHTML = vote.jokawi
}
