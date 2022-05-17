(function(){
  window.chatInfo = {}

  var chat = {
    messageToSend: '',
    messageResponses: [
      'Why did the web developer leave the restaurant? Because of the table layout.',
      'How do you comfort a JavaScript bug? You console it.',
      'An SQL query enters a bar, approaches two tables and asks: "May I join you?"',
      'What is the most used language in programming? Profanity.',
      'What is the object-oriented way to become wealthy? Inheritance.',
      'An SEO expert walks into a bar, bars, pub, tavern, public house, Irish pub, drinks, beer, alcohol'
    ],
    init: function() {
      this.cacheDOM();
      this.bindEvents();
      this.render();
    },
    cacheDOM: function() {
      this.$chatHistory = $('.chat-history');
      this.$button = $('button');
      this.$textarea = $('#message-to-send');
      this.$chatHistoryList =  this.$chatHistory.find('ul');
    },
    bindEvents: function() {
      this.$button.on('click', this.addMessage.bind(this));
      this.$textarea.on('keyup', this.addMessageEnter.bind(this));
    },
    render: function() {
      this.scrollToBottom();
      if (this.messageToSend.trim() !== '') {
        var template = Handlebars.compile( $("#message-template").html());
        var context = { 
          messageOutput: this.messageToSend,
          time: this.getCurrentTime()
        };

        this.$chatHistoryList.append(template(context));
        this.scrollToBottom();
        this.$textarea.val('');
        
        // responses
        var templateResponse = Handlebars.compile( $("#message-response-template").html());
        var contextResponse = { 
          response: this.getRandomItem(this.messageResponses),
          time: this.getCurrentTime()
        };
        
        setTimeout(function() {
          this.$chatHistoryList.append(templateResponse(contextResponse));
          this.scrollToBottom();
        }.bind(this), 1500);
        
      }
      
    },
    
    addMessage: function() {
      console.log(22222);
      this.messageToSend = this.$textarea.val()
      this.render();         
    },
    addMessageEnter: function(event) {
            console.log(2222222222);

        // enter was pressed
        if (event.keyCode === 13) {
          this.addMessage();
        }
    },
    scrollToBottom: function() {
       $('.chat-history').scrollTop(10000);
    },
    getCurrentTime: function() {
      return new Date().toLocaleTimeString().
              replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
    },
    getRandomItem: function(arr) {
      return arr[Math.floor(Math.random()*arr.length)];
    }
    
  };
  
  chat.init();
  
  var searchFilter = {
    options: { valueNames: ['name'] },
    init: function() {
      var userList = new List('people-list', this.options);
      var noItems = $('<li id="no-items-found">No items found</li>');
      
      userList.on('updated', function(list) {
        if (list.matchingItems.length === 0) {
          $(list.list).append(noItems);
        } else {
          noItems.detach();
        }
      });
    }
  };

  searchFilter.init();

      
  Handlebars.registerHelper('JSON', function (object) {
      return JSON.stringify(object);
  })


  $(document).on("click", "li.contact-info", function(){
    var template = Handlebars.compile($("#contact-detail-template").html());
    $(".chat-header").html(template($(this).data("info")));
    window.chatInfo.id = $(this).data("info").id;
    getMessage();

  })

  function getMessage(){
          window.socket.emit("getMessageList", window.chatInfo.id, function(data)  {
        var data = JSON.parse(data)
        var template = Handlebars.compile($("#message-detail-template").html());

        $(".message-content").html(template({"messages":data}));
        $('.chat-history').scrollTop(10000);
      })
  }

  $(document).on('keyup', "#message-to-send", function(enent){
        if (event.keyCode === 13) {
          window.socket.emit("sendMessage", {"content":$('#message-to-send').val(), "id":window.chatInfo.id}, function(data)  {
            console.log(data);
            getMessage();
          });
          $('#message-to-send').val("");
        }
  });


  const socket = io("http://127.0.0.1:9999");
  window.socket = socket;

  socket.on('connect', function() {
    socket.emit("getContactList", "", function(data)  {
      var data = JSON.parse(data)
      var template = Handlebars.compile($("#contact-list-template").html());

      $(".contact-list").html(template({"contactList":data}));
      $(".contact-info").get(0).click();
    });
  });

  socket.on('receiverMessage', function(data) {
    if(data == window.chatInfo.id){
      getMessage();
    }

  });


})();