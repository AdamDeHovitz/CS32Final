bulkAppControllers.controller("eventCtrl", 
	function($scope, $http, $rootScope, $stateParams, $ionicPopup, $state) {

	var eventId = $stateParams.eventId;
	console.log(eventId);
	console.log($state.current.name);
	if ($state.current.name == "tab.events-event") {
		console.log("events")
		$scope.curProfileUrl = "events-profile";
	} else {
		$scope.curProfileUrl = "profile";
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


	$scope.confirmJoin = function() {
		console.log("joining");
		$scope.isMember = true;
		$scope.event.CurMemberNum = $scope.event.CurMemberNum + 1;
		/*$.post("/event-join", {id: $rootScope.account.id, eventId: $scope.eventId}, 
		function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
		}*/
	}

	$scope.confirmClose = function() {
		console.log("closing");
	}

	$scope.confirmLeave = function() {
		console.log("leaving");
		$scope.event.CurMemberNum = $scope.event.CurMemberNum - 1;

		$scope.isMember = false;
	}
	 
	$.post("/event-view", {id: eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
			var isOwner = responseObject.authorId == $rootScope.account.id;
			var isMember = false;
			for (var i = 0; i < responseObject.members.length; i++) {
				if (responseObject.members[i] == $rootScope.account.id) {
					isMember = true;
				}
			}
			$scope.isOwner = isOwner;
			$scope.isMember = isMember;
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