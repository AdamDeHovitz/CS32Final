bulkAppControllers.controller("chatCtrl", 
		function($scope, $rootScope, $state, $stateParams, MockService,
				$ionicActionSheet, $ionicPopup, $ionicScrollDelegate, $timeout, $interval) {

			    // mock acquiring data via $stateParams
			    $scope.toUser = {
			      _id: '534b8e5aaa5e7afc1b23e69b',
			      pic: 'http://ionicframework.com/img/docs/venkman.jpg',
			      username: 'Venkman'
			    }

			    // this could be on $rootScope rather than in $stateParams
			    $scope.user = {
			      _id: $rootScope.account.id,
			      pic: $rootScope.account.img,
			      username: $rootScope.account.name
			    };

			    $scope.input = {
			      message: localStorage['userMessage-' + $scope.toUser._id] || ''
			    };

			    var messageCheckTimer;

			    var viewScroll = $ionicScrollDelegate.$getByHandle('userMessageScroll');
			    var footerBar; // gets set in $ionicView.enter
			    var scroller;
			    var txtInput; // ^^^

			    $scope.$on('$ionicView.enter', function() {
			      console.log('UserMessages $ionicView.enter');

			      getMessages();

			      $timeout(function() {
			        footerBar = document.body.querySelector('#userMessagesView .bar-footer');
			        scroller = document.body.querySelector('#userMessagesView .scroll-content');
			        txtInput = angular.element(footerBar.querySelector('textarea'));
			      }, 0);

			      messageCheckTimer = $interval(function() {
			        // here you could check for new messages if your app doesn't
					// use push notifications or user disabled them
			      }, 20000);
			    });

			    $scope.$on('$ionicView.leave', function() {
			      console.log('leaving UserMessages view, destroying interval');
			      // Make sure that the interval is destroyed
			      if (angular.isDefined(messageCheckTimer)) {
			        $interval.cancel(messageCheckTimer);
			        messageCheckTimer = undefined;
			      }
			    });

			    $scope.$on('$ionicView.beforeLeave', function() {
			      if (!$scope.input.message || $scope.input.message === '') {
			        localStorage.removeItem('userMessage-' + $scope.toUser._id);
			      }
			    });

			    function getMessages() {
			      // the service is mock but you would probably pass the
			    	// toUser's GUID here
			      MockService.getUserMessages({
			        toUserId: $scope.toUser._id
			      }).then(function(data) {
			        $scope.doneLoading = true;
			        $scope.messages = data.messages;

			        $timeout(function() {
			          viewScroll.scrollBottom();
			        }, 0);
			      });
			    }

			    $scope.$watch('input.message', function(newValue, oldValue) {
			      //console.log('input.message $watch, newValue ' + newValue);
			      if (!newValue) newValue = '';
			      localStorage['userMessage-' + $scope.toUser._id] = newValue;
			    });

			    $scope.sendMessage = function(sendMessageForm) {
			      var message = {
			        toId: $scope.toUser._id,
			        text: $scope.input.message
			      };

			      // if you do a web service call this will be needed as well
					// as before the viewScroll calls
			      // you can't see the effect of this in the browser it needs
					// to be used on a real device
			      // for some reason the one time blur event is not firing in
					// the browser but does on devices
			      keepKeyboardOpen();

			      // MockService.sendMessage(message).then(function(data) {
			      $scope.input.message = '';

			      //message._id = new Date().getTime(); // :~)
			      message.date = new Date();
			      message.username = $scope.user.username;
			      message.userId = $scope.user._id;
			      message.pic = $scope.user.picture;

			      console.log(message);
			      $scope.messages.push(message);

			      $timeout(function() {
			        keepKeyboardOpen();
			        viewScroll.scrollBottom(true);
			      }, 0);

			      // mock message
			      /*
			      $timeout(function() {
			        $scope.messages.push(MockService.getMockMessage());
			        keepKeyboardOpen();
			        viewScroll.scrollBottom(true);
			      }, 2000);*/

			      console.log("sending chat message");
			      console.log(JSON.stringify(message));
			      webSocket.send(JSON.stringify(message));

			      // });
			    };

			    //ng - class = "{'has-error': forms.loginForm.password.$touched && forms.loginForm.password.$invalid }" 
			    function keepKeyboardOpen() {
			      txtInput.one('blur', function() {
			        txtInput[0].focus();
			      });
			    }

			    $scope.onMessageHold = function(e, itemIndex, message) {
			      //console.log('message: ' + JSON.stringify(message, null, 2));
			      $ionicActionSheet.show({
			        buttons: [{
			          text: 'Copy Text'
			        }, {
			          text: 'Delete Message'
			        }],
			        buttonClicked: function(index) {
			          switch (index) {
			            case 0: // Copy Text
			              // cordova.plugins.clipboard.copy(message.text);

			              break;
			            case 1: // Delete
			              // no server side secrets here :~)
			              $scope.messages.splice(itemIndex, 1);
			              $timeout(function() {
			                viewScroll.resize();
			              }, 0);

			              break;
			          }

			          return true;
			        }
			      });
			    };

			    // this prob seems weird here but I have reasons for this in my
				// app, secret!
			    $scope.viewProfile = function(msg) {
			      if (msg.userId === $scope.user._id) {
			        // go to your profile
			      } else {
			        // go to other users profile
			      }
			    };

			    // I emit this event from the monospaced.elastic directive, read
				// line 480
			    $scope.$on('taResize', function(e, ta) {
			      //console.log('taResize');
			      //console.log(ta);
			      if (!ta) return;

			      var taHeight = ta[0].offsetHeight;
			      //console.log('taHeight: ' + taHeight);
			      
			      if (!footerBar) return;
			      
			      var newFooterHeight = taHeight + 10;
			      newFooterHeight = (newFooterHeight > 44) ? newFooterHeight : 44;
			      footerBar.style.height = newFooterHeight + 'px';
			      scroller.style.bottom = newFooterHeight + 49 + 'px';
			    });

			    // Sockets
			    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
			    webSocket.onopen = function() { console.log($scope.user.username + " joined chat yep"); };
			    webSocket.onmessage = function (msg) { updateChat(msg); };
				webSocket.onclose = function () { alert("WebSocket connection closed") };

				function updateChat(msg) {
				    var data = JSON.parse(JSON.parse(msg.data).userMessage);
				    console.log(data);
				}



});