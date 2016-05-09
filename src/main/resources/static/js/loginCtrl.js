
bulkAppControllers.controller("loginCtrl", function($scope, $rootScope, $state, $ionicViewSwitcher, $ionicModal, $http, $ionicPopup, $interval,
    $timeout, toaster) {


	$scope.loginData = {};
	$scope.registerData = {};
	$scope.forms = {};

	//$scope.fd = = new FormData();

	$scope.$on('$ionicView.enter', function() {
		$scope.forms.registerForm.$setPristine();
		$scope.forms.registerForm.$setUntouched();
	});

	$ionicModal.fromTemplateUrl('register-modal.html', {
	  scope : $scope,
	  animation : 'slide-in-up'
	}).then(function(modal) {
		$scope.modal = modal;
	});
	$scope.openModal = function() {
		$scope.modal.show();
	};
	$scope.closeModal = function() {
		$scope.modal.hide();
		$scope.registerData = {};
		$scope.forms.registerForm.$setPristine();
		$scope.forms.registerForm.$setUntouched();
	};
	// Cleanup the modal when we're done with it!
	$scope.$on('$destroy', function() {
		$scope.modal.remove();
	});
	// Execute action on hide modal
	$scope.$on('modal.hidden', function() {
		// Execute action
	});
	// Execute action on remove modal
	$scope.$on('modal.removed', function() {
		// Execute action
	});

	$rootScope.stopNotifications = undefined;

	var getNotifications = function() {
		// post request for notifications

		$.post("/notification", {
			"id" : $rootScope.account.id
		}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
			var body = "";
			var title = "";
			var route = undefined;
			var urlPrefix = "";
			var hasNotif = false;
			
			if ($state.current.name.includes("tab.events") || $state.current.name == "tab.my-events"
				|| $state.current.name == "tab.joined-events" || $state.current.name == "tab.pending-events") {
				urlPrefix = "tab.events-";
			} else if ($state.current.name.includes("tab.account") || $state.current.name == "tab.settings"
				|| $state.current.name == "tab.help") {
				urlPrefix = "tab.account-";
			} else {
				urlPrefix = "tab.";
			}
			console.log($state.current);


			if (responseObject.messages.length > 0 && responseObject.messages[0]) {
				hasNotif = true;
				var notif = responseObject.messages[0];
				var title = "New Message in \"" + notif.eventName + "\"";
				var body = notif.username + ": " + notif.content;
				route = function() {
					$state.go(urlPrefix + 'chat', { eventId : notif.eventId });
				};
			} else if (responseObject.requests.length > 0
			    && responseObject.requests[0]) {
				hasNotif = true;
				var notif = responseObject.requests[0];
				var requester = notif.userRequester.prof.firstName + " "
				    + notif.userRequester.prof.lastName;
				var eventName = notif.name;
				body = requester + " has requested to join " + eventName;
				title = "Request to Join";
				route = function() {
					$state.go(urlPrefix + 'event', { eventId : notif.id });
				};
			} else if (responseObject.joinedEvents.length > 0
			    && responseObject.joinedEvents[0]) {
				hasNotif = true;
				var notif = responseObject.joinedEvents[0];
				body = "You have been accepted to \"" + notif.name + "\"."
				title = "Event Acceptance";
				route = function() {
					$state.go(urlPrefix + 'event', {eventId : notif.id});
				};

			} else if (responseObject.eventState.length > 0
			    && responseObject.eventState[0]) {
				hasNotif = true;
				var notif = responseObject.eventState[0];
				route = function() {
					$state.go(urlPrefix + 'event', {eventId : notif.id});
				};
				if (notif.state == "CLOSED") {
					title = "Event Closed";
					body = "\"" + notif.name + "\" has been closed to the public.";
				} else if (notif.state = "STARTED") {
					title = "Event Starting";
					body = "\"" + notif.name + "\" is starting! Check for updates.";
				}
				// notification if ended?
			}
			
			if (hasNotif) {
				var f = function() {
					console.log("notif routing");
					toaster.clear();
					route();
				}
				toaster.pop({type: 'note', title: title, body: body, 
					timeout: 50000, clickHandler: f, showCloseButton: false});
			}
			
		});
 };

	$scope.doRegister = function() {
		console.log('Doing register', $scope.registerData);

		var curData = $scope.registerData;
		var regisData = {
		  firstName : curData.firstName,
		  lastName : curData.lastName,
		  email : curData.email,
		  password : curData.password,
		  image : curData.img
		};
		console.log(regisData);

		$.post("/register", {
		  "firstName" : curData.firstName,
		  "lastName" : curData.lastName,
		  "email" : curData.email,
		  "password" : curData.password,
		  "image" : curData.img
		}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
			if (responseObject.hasError) {
				$scope.showAlert('Registration Failed', responseObject.errorMsg);
			} else {
				$rootScope.account = {
				  name : responseObject.name,
				  id : responseObject.id,
				  joined : responseObject.date,
				  rating : 5,
				  img : responseObject.picture,
				  reviews : responseObject.reviews
				};
				$timeout($scope.closeModal, 3000);
				$rootScope.authenticated = true;
				$rootScope.stopNotifications = $interval(getNotifications, 3000);
				$ionicViewSwitcher.nextDirection('forward');
				$state.go("tab.feed");
			}
		});
	};

	$scope.showAlert = function(curTitle, curMsg) {
		var alertPopup = $ionicPopup.alert({
		  title : curTitle,
		  template : curMsg
		});

		alertPopup.then(function(res) {
		});
	};

	$scope.doLogin = function() {
		console.log('Doing login', $scope.loginData);

		$.post("/login", {
		  "username" : $scope.loginData.email,
		  "password" : $scope.loginData.password,
		}, function(responseJSON) {
			console.log(responseJSON);
			responseObject = JSON.parse(responseJSON);
			if (responseObject.hasError) {
				$scope.showAlert('Authentication Failed',
				    'Username or password is incorrect.');
			} else {
				$rootScope.account = {
				  name : responseObject.name,
				  id : responseObject.id,
				  joined : responseObject.date,
				  rating : 5,
				  img : responseObject.picture,
				  reviews : responseObject.reviews
				};
				$rootScope.authenticated = true;
				$rootScope.stopNotifications = $interval(getNotifications, 5000);
				$ionicViewSwitcher.nextDirection('forward');
				$state.go("tab.feed");
			};
			
			$timeout(function() {
				$scope.loginData = {};
				$scope.forms.loginForm.$setPristine();
				$scope.forms.loginForm.$setUntouched();
			}, 2000);
		});
	};
});

/*
 * bulkAppcontrollers.controller("loginCtrl", ['$scope', '$http', '$rootScope',
 * '$location', function($scope, $http) { $scope.longin = function(username,
 * password) { var postData = $.param({ json: JSON.stringify({ username:
 * username, password: password }) }); $http.post("/login",
 * postData).success(function(data, status) { $rootScope.userId = data.userID;
 * $location.path("/home"); }); }
 * 
 * }]);
 */