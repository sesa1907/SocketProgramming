const EventEmmiter = require('events')
const WebSocket = require('ws')
const { v4: uuid } = require('uuid')

class WebSocketServer extends EventEmmiter {
  constructor({ server }) {
    super()
    this.presidents = {
      jokawi: 0,
      kobami: 0
    }
    this.wss = new WebSocket.Server({ server })
    this.sockets = Object.create(null)

    this.wss.on('connection', (ws, req) => {

      let id = uuid()
      this.sockets[id] = ws

      ws.on('message', message => {
        try {
          let msg = JSON.parse(message)
          this.emit(msg.type, id, msg)
        } catch (e) {
          console.warn('invalid message', message)
          console.error(e)
        }
      })

      ws.on('close', () => {
        delete this.sockets[id]
      })

      this.send(id, { type: "welcome", id: id, vote: this.presidents })
    })
  }

  broadcast(msg) {
    for (const key in this.sockets) {
      this.send(key, msg)
    }
  }

  send(id, msg) {
    if (this.sockets[id]) {
      this.sockets[id].send(JSON.stringify(msg))
    } else {
      console.warn(`no socket with the id ${id}`)
    }
  }

  deleteId(id) {
    delete this.sockets[id]
  }
}

module.exports = WebSocketServer
