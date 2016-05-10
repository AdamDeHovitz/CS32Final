bulkAppControllers.controller("eventCtrl", function($scope, $http, $rootScope,
    $stateParams, $ionicPopup, $state, $interval) {

	var eventId = $stateParams.eventId;
	console.log(eventId);
	console.log($state.current.name);
	if ($state.current.name == "tab.events-event") {
		console.log("events")
		$scope.curProfileUrl = "events-profile";
		$scope.curChatUrl = "events-chat";
		$scope.curRequestsUrl = "events-requests";
		$scope.curMapUrl = "events-map";
		$scope.curMembersUrl = "events-members";
	} else {
		$scope.curProfileUrl = "profile";
		$scope.curChatUrl = "chat";
		$scope.curRequestsUrl = "requests";
		$scope.curMapUrl = "map";
		$scope.curMembersUrl = "members";
	};

	$scope.getEventInfo = function() {
		$.post("/event-view", {id : eventId, userId : $rootScope.account.id}, 
				function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
			$scope.newlyAccepted = responseObject.newlyAccepted;
			var isOwner = responseObject.authorId == $rootScope.account.id;
			var isMember = false;
			var requestedJoin = false;
			for (var i = 0; i < responseObject.members.length; i++) {
				if (responseObject.members[i] == $rootScope.account.id) {
					isMember = true;
				};
			};
			
			if (responseObject.newRequestNum > 0) {
				$scope.hasRequests = true;
				$scope.requestNum = responseObject.newRequestNum;
			};
			if (responseObject.newMessageNum > 0) {
				$scope.hasMessages = true;
				$scope.messageNum = responseObject.newMessageNum;
			};

			for (var i = 0; i < responseObject.requests.length; i++) {
				if (responseObject.requests[i] == $rootScope.account.id) {
					requestedJoin = true;
				};
			};

			$scope.isOwner = isOwner;
			$scope.isMember = isMember;
			$scope.requestedJoin = requestedJoin;
			console.log(responseObject);
		});
	};

	$scope.getEventInfo();
	$scope.updateEvent = $interval($scope.getEventInfo, 5000);

	$scope.$on('$ionicView.enter', function() {
		// var updateEvent = $interval($scope.getEventInfo(), 5000);
	});
	$scope.$on('$ionicView.leave', function() {
		$interval.cancel($scope.updateEvent);
	});

	// A confirm dialog
	$scope.showConfirm = function(title, message, success) {
		var confirmPopup = $ionicPopup.confirm({
		  title : title,
		  template : message
		});

		confirmPopup.then(function(res) {
			if (res) {
				success();
			} else {
				console.log('You are not sure');
			}
		});
	};


	$scope.requestJoin = function() {
		console.log("requesting");
		var postParams = {
		  id : $rootScope.account.id,
		  eventId : eventId
		};
		$scope.requestedJoin = true;
		console.log(postParams);
		$.post("/event-request", postParams, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
		});
	};

	/*
	 * $scope.confirmJoin = function() { console.log("joining"); $scope.isMember =
	 * true; $scope.event.CurMemberNum = $scope.event.CurMemberNum + 1; var
	 * postParams = {id: $rootScope.account.id, eventId: eventId};
	 * console.log(postParams); $.post("/event-join", postParams,
	 * function(responseJSON) { responseObject = JSON.parse(responseJSON);
	 * console.log(responseObject); }); };
	 */

	$scope.confirmClose = function() {
		if ($rootScope.account.id == $scope.event.authorId) {
			$scope.event.state = "CLOSED";
			var postParams = {
			  id : $rootScope.account.id,
			  eventId : eventId
			};
			$.post("/event-close", postParams, function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
			});
		}
		console.log("closing");
	}

	$scope.confirmStart = function() {
		if ($rootScope.account.id == $scope.event.authorId) {
			$scope.event.state = "STARTED";
			var postParams = {
			  id : $rootScope.account.id,
			  eventId : eventId
			};
			$.post("/event-start", postParams, function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
			});
		}
	}

	$scope.confirmRemove = function() {
		if ($rootScope.account.id == $scope.event.authorId) {
			$scope.event.state = "REMOVED";
			var postParams = {
			  id : $rootScope.account.id,
			  eventId : eventId
			};
			
			$.post("/delete-event", {id: $scope.account.id, eventId: eventId}, function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
				$state.go("tab.events");
				// figure out where to route
			});
			/*
			 * $.post("/event-remove", postParams, function(responseJSON) {
			 * responseObject = JSON.parse(responseJSON); console.log(responseObject);
			 * });
			 */
		}
	}

	$scope.confirmLeave = function() {
		console.log("leaving");
		$scope.event.CurMemberNum = $scope.event.CurMemberNum - 1;
		var postParams = {
		  id : $rootScope.account.id,
		  eventId : eventId
		};
		$.post("/event-remove", postParams, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
		});

		$scope.isMember = false;
	}

	$scope.goAuthor = function() {
		if ($state.current.name == "tab.events-event") {
			$state.go('tab.events-profile', {
				userId : $scope.event.authorId
			});
		} else {
			$state.go('tab.profile', {
				userId : $scope.event.authorId
			});
		}
	}
		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/

	/*
	 * if (!$rootScope.userId) { $location.path("/"); } var postData = $.param({
	 * json: JSON.stringify({ userId: userId, }) }); $http.post("/feedEntries",
	 * postData).success(function(data, status) { $scope.events = data.entries;
	 * });
	 */
});

