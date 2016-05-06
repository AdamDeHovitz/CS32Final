bulkAppControllers.controller("eventCtrl", 
	function($scope, $http, $rootScope, $stateParams, $ionicPopup, $state) {

	var eventId = $stateParams.eventId;
	console.log(eventId);
	console.log($state.current.name);
	if ($state.current.name == "tab.events-event") {
		console.log("events")
		$scope.curProfileUrl = "events-profile";
		$scope.curChatUrl = "events-chat";
		$scope.curRequestsUrl = "events-requests"
		$scope.curMapUrl = "events-map"
	} else {
		$scope.curProfileUrl = "profile";
		$scope.curChatUrl = "chat";
		$scope.curRequestsUrl = "requests"
		$scope.curMapUrl = "map"
	}
	
	 // A confirm dialog
	 $scope.showConfirm = function(title, message, success) {
	   var confirmPopup = $ionicPopup.confirm({
	     title: title,
	     template: message
	   });

	   confirmPopup.then(function(res) {
	     if(res) {
		 	success();
		 } else {
	       console.log('You are not sure');
	     }
	   });
	 };



	$scope.requestJoin = function() {
		console.log("requesting");
		var postParams = {id: $rootScope.account.id, eventId: eventId};
		$scope.requestedJoin = true;
		console.log(postParams);
		$.post("/event-request", postParams, 
			function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
		});
	};

	 /*
	$scope.confirmJoin = function() {
		console.log("joining");
		$scope.isMember = true;
		$scope.event.CurMemberNum = $scope.event.CurMemberNum + 1;
		var postParams = {id: $rootScope.account.id, eventId: eventId};
		console.log(postParams);
		$.post("/event-join", postParams, 
			function(responseJSON) {
				responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
		});
	};*/

	$scope.confirmClose = function() {
		console.log("closing");
	}

	$scope.confirmLeave = function() {
		console.log("leaving");
		$scope.event.CurMemberNum = $scope.event.CurMemberNum - 1;
		var postParams = {id: $rootScope.account.id, eventId: eventId};
		$.post("/event-remove", postParams, 
				function(responseJSON) {
					responseObject = JSON.parse(responseJSON);
					console.log(responseObject);
		});

		$scope.isMember = false;
	}
	 
	$.post("/event-view", {id: eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
			var isOwner = responseObject.authorId == $rootScope.account.id;
			var isMember = false;
			var requestedJoin = false;
			for (var i = 0; i < responseObject.members.length; i++) {
				if (responseObject.members[i] == $rootScope.account.id) {
					isMember = true;
				}
			}
			/*
			for (var i = 0; i < responseObject.requestedMembers.length; i++) {
				if (responseObject.members[i] == $rootScope.account.id) {
					requestedJoin = true;
				}
			}*/
			$scope.isOwner = isOwner;
			$scope.isMember = isMember;
			$scope.requestedJoin = requestedJoin;
			console.log(responseObject);
	});

	
	 $scope.goAuthor = function() {
	 	if ($state.current.name == "tab.events-event") {
	 		$state.go('tab.events-profile',{userId: $scope.event.authorId});
	 	} else {
		 	$state.go('tab.profile',{userId: $scope.event.authorId});
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
});


